package ai.devpath.learning.seed;

import ai.devpath.learning.assessment.QuestionBankRepository;
import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** 로컬(dev) 끝단간용 MD2 승인 문항 시드. 비어있을 때만 전체 적재. */
@Component
@Profile("dev")
public class QuestionBankSeeder implements CommandLineRunner {

  private static final long MD2_SEED_COUNT = 500L;

  private final QuestionBankRepository questions;
  private final DataSource dataSource;

  public QuestionBankSeeder(QuestionBankRepository questions, DataSource dataSource) {
    this.questions = questions;
    this.dataSource = dataSource;
  }

  @Override
  public void run(String... args) {
    long count = questions.count();
    if (count >= MD2_SEED_COUNT) {
      return;
    }
    if (count > 0) {
      throw new IllegalStateException(
          "question_bank has partial seed data (" + count + " rows); expected 0 or >= 500");
    }
    var populator = new ResourceDatabasePopulator(
        new ClassPathResource("db/seed/question_bank_md2_seed.sql"));
    populator.execute(dataSource);
  }
}
