plugins {
	java
	id("org.springframework.boot") version "4.0.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ai.devpath"
version = "0.0.1-SNAPSHOT"
description = "DevPath AI learning services (onboarding, path engine, content, mentor)"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://maven.pkg.github.com/DevPathAi/devpath-shared")
		credentials {
			username = providers.gradleProperty("gpr.user").orElse(providers.environmentVariable("GITHUB_ACTOR")).orNull
			password = providers.gradleProperty("gpr.token").orElse(providers.environmentVariable("GITHUB_TOKEN")).orNull
		}
	}
}

val contentGenSourceSet = sourceSets.create("contentGen") {
	java.srcDir("src/contentGen/java")
	resources.srcDir("tools/content-gen")
	compileClasspath += sourceSets["main"].runtimeClasspath
	runtimeClasspath += output + compileClasspath
}

configurations.named("contentGenImplementation") {
	extendsFrom(configurations["implementation"])
}

configurations.named("contentGenRuntimeOnly") {
	extendsFrom(configurations["runtimeOnly"])
}

val knowledgeGenSourceSet = sourceSets.create("knowledgeGen") {
	java.srcDir("src/knowledgeGen/java")
	// contentGen의 ContentChunker/ContentChunk를 재사용한다
	compileClasspath += sourceSets["main"].runtimeClasspath + contentGenSourceSet.output
	runtimeClasspath += output + compileClasspath
}

configurations.named("knowledgeGenImplementation") {
	extendsFrom(configurations["implementation"])
}

configurations.named("knowledgeGenRuntimeOnly") {
	extendsFrom(configurations["runtimeOnly"])
}

tasks.named("compileKnowledgeGenJava") {
	dependsOn(tasks.named("compileContentGenJava"))
}

sourceSets.named("test") {
	compileClasspath += contentGenSourceSet.output
	runtimeClasspath += contentGenSourceSet.output
	compileClasspath += knowledgeGenSourceSet.output
	runtimeClasspath += knowledgeGenSourceSet.output
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("ai.devpath:devpath-shared:0.0.1-SNAPSHOT")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-kafka")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("net.jqwik:jqwik:1.9.2")
	// Boot 4는 autoconfigure가 모듈 분리됨 → Flyway 자동구성(FlywayAutoConfiguration)은 spring-boot-flyway에 있다.
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.awaitility:awaitility")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
	testImplementation("org.springframework.boot:spring-boot-flyway")
	testImplementation("org.flywaydb:flyway-core")
	testImplementation("org.flywaydb:flyway-database-postgresql")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named("compileTestJava") {
	dependsOn(tasks.named("compileContentGenJava"))
	dependsOn(tasks.named("compileKnowledgeGenJava"))
}

tasks.register<JavaExec>("validateQuestions") {
	group = "content generation"
	description = "Validate approved MD2 diagnostic question JSONL."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.question.ValidateQuestionsCommand")
	args("tools/content-gen/generated/approved/questions.jsonl")
}

tasks.register<JavaExec>("reviewQuestionsLocal") {
	group = "content generation"
	description = "Review approved questions with Claude. Do not run in CI."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.question.ReviewQuestionsCommand")
	args(
		"tools/content-gen/generated/approved/questions.jsonl",
		(project.findProperty("track") as String? ?: ""),
		"tools/content-gen/generated/review"
	)
}

tasks.register<JavaExec>("makeQuestionSeedSql") {
	group = "content generation"
	description = "Create deterministic question_bank seed SQL from approved JSONL."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.question.MakeQuestionSeedSqlCommand")
	args(
		"tools/content-gen/generated/approved/questions.jsonl",
		"tools/content-gen/generated/seeds/question_bank_seed.sql",
		"src/main/resources/db/seed/question_bank_md2_seed.sql",
		"src/test/resources/seed/question_bank_md2_seed.sql",
		"src/test/resources/seed/question_bank_seed.sql"
	)
}

tasks.register<JavaExec>("generateQuestionsLocal") {
	group = "content generation"
	description = "Generate draft questions from local Ollama. Do not run in CI."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.question.GenerateQuestionsCommand")
	args(
		providers.gradleProperty("ollama.model").orElse("qwen2.5:7b").get(),
		providers.gradleProperty("track").orElse("").get()
	)
}

tasks.register<JavaExec>("validateContents") {
	group = "content generation"
	description = "Validate approved MD2 learning content JSONL."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.content.ValidateContentsCommand")
	args("tools/content-gen/generated/approved/contents.jsonl")
}

tasks.register<JavaExec>("makeContentSeedSql") {
	group = "content generation"
	description = "Create deterministic contents and content_embeddings seed SQL from approved JSONL."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.content.MakeContentSeedSqlCommand")
	args(
		"tools/content-gen/generated/approved/contents.jsonl",
		"tools/content-gen/generated/approved/content_embeddings.jsonl",
		"tools/content-gen/generated/seeds/content_seed.sql",
		"src/main/resources/db/seed/content_md2_seed.sql",
		"src/test/resources/seed/content_md2_seed.sql"
	)
}

tasks.register<JavaExec>("generateContentsLocal") {
	group = "content generation"
	description = "Generate draft learning contents from local Ollama. Do not run in CI."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.content.GenerateContentsCommand")
	args(
		providers.gradleProperty("ollama.model").orElse("qwen2.5:7b").get(),
		providers.gradleProperty("track").orElse("").get()
	)
}

tasks.register<JavaExec>("embedContentsLocal") {
	group = "content generation"
	description = "Generate local nomic-embed-text embeddings for approved contents. Do not run in CI."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.content.EmbedContentsCommand")
	args(
		"tools/content-gen/generated/approved/contents.jsonl",
		"tools/content-gen/generated/approved/content_embeddings.jsonl",
		providers.gradleProperty("ollama.embedModel").orElse("nomic-embed-text").get()
	)
}

tasks.register<JavaExec>("scanKnowledgeDocs") {
	group = "knowledge base"
	description = "Scan study documents into documents.jsonl. Do not run in CI."
	classpath = knowledgeGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.knowledgegen.ScanKnowledgeDocsCommand")
	args("D:/workspace/dsd", "tools/knowledge-gen/generated/documents.jsonl")
}

tasks.register<JavaExec>("embedKnowledge") {
	group = "knowledge base"
	description = "Chunk + batch-embed study documents. Requires local Ollama. Do not run in CI."
	classpath = knowledgeGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.knowledgegen.EmbedKnowledgeCommand")
	// sourceCommit 검증은 태스크 realize 시점이 아니라 실행(doFirst) 시점에 일어나야 한다 —
	// register{} 람다 안에서 바로 던지면 이 태스크가 realize되기만 해도(예: `gradle tasks`,
	// IDE gradle sync) 무관한 명령까지 깨진다. args()는 여기 doFirst 안에서만 설정한다 —
	// 밖에서도 설정하면 args()가 누적(append)돼 인자가 중복된다.
	doFirst {
		val sourceCommit = project.findProperty("sourceCommit")?.toString()
			?: throw GradleException("-PsourceCommit=<dsd master SHA> 를 지정하라 (설계 §7.4)")
		args(
			"tools/knowledge-gen/generated/documents.jsonl",
			"tools/knowledge-gen/generated/embeddings.jsonl",
			sourceCommit,
			"nomic-embed-text"
		)
	}
}

tasks.register<JavaExec>("loadKnowledge") {
	group = "knowledge base"
	description = "Load embeddings.jsonl into the knowledge base. Do not run in CI."
	classpath = knowledgeGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.knowledgegen.LoadKnowledgeCommand")
	args("tools/knowledge-gen/generated/embeddings.jsonl", "develop-study-documents")
}
