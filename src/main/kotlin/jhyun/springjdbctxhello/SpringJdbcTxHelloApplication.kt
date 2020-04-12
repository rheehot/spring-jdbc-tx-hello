package jhyun.springjdbctxhello

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionException
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate

@SpringBootApplication
class SpringJdbcTxHelloApplication

fun main(args: Array<String>) {
    runApplication<SpringJdbcTxHelloApplication>(*args)
}


@Table(value = "posts")
data class Post(
        @Id
        @Column(value = "id")
        var id: Int? = null,

        @Column(value = "title")
        val title: String
)

interface PostRepository : CrudRepository<Post, Int>

@Service
open class PostService(
        @Autowired
        private val postRepository: PostRepository,

        @Autowired
        private val transactionManager: PlatformTransactionManager
) {
    private val LOG = LoggerFactory.getLogger(PostService::class.java)


    @Transactional(readOnly = true)
    fun findAll(): MutableIterable<Post> {
        return postRepository.findAll()
    }

    @Transactional
    fun doSome() {
        postRepository.deleteById(2)
        //
        val transactionTemplate = TransactionTemplate(transactionManager).apply {
            propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
        }
        transactionTemplate.execute {
            try {
                postRepository.deleteById(3)
                throw Exception("exception")
            } catch (e: Exception) {
                LOG.warn("caught: {}", e)
                it.setRollbackOnly()
            }
        }
        //
        postRepository.deleteById(1)
    }
}