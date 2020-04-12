package jhyun.springjdbctxhello

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class SpringJdbcTxHelloApplicationTests(
        @Autowired
        private val postService: PostService
) {
    private val LOG = LoggerFactory.getLogger(SpringJdbcTxHelloApplicationTests::class.java)

    @Test
    fun contextLoads() {
        assertNotNull(postService)
    }

    @Sql("classpath:sqls/posts.sql")
    @Test
    fun f() {
		val allBefore = postService.findAll().toList()
		LOG.debug("ALL = {}", allBefore)
		assertEquals(allBefore.size, 3)
		//
		postService.doSome()
		val allAfter = postService.findAll().toList()
		assertEquals(allAfter.size, 1)
        assertEquals(allAfter[0].title, "three")
	}

}
