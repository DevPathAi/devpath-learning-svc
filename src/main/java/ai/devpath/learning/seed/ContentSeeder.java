package ai.devpath.learning.seed;

import ai.devpath.learning.path.ContentRepository;
import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** 로컬(dev) 끝단간용 MD2 승인 콘텐츠 시드. 비어있을 때만 전체 적재. */
@Component
@Profile("dev")
public class ContentSeeder implements CommandLineRunner {

  private static final long MD2_SEED_COUNT = 150L;

  private final ContentRepository contents;
  private final DataSource dataSource;

  public ContentSeeder(ContentRepository contents, DataSource dataSource) {
    this.contents = contents;
    this.dataSource = dataSource;
  }

  @Override
  public void run(String... args) {
    long count = contents.count();
    if (count >= MD2_SEED_COUNT) {
      return;
    }
    if (count > 0) {
      throw new IllegalStateException(
          "contents has partial seed data (" + count + " rows); expected 0 or >= 150");
    }
    var populator = new ResourceDatabasePopulator(
        new ClassPathResource("db/seed/content_md2_seed.sql"));
    populator.execute(dataSource);
  }
}
