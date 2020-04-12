package jhyun.springjdbctxhello

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringJdbcTxHelloApplication

fun main(args: Array<String>) {
	runApplication<SpringJdbcTxHelloApplication>(*args)
}
