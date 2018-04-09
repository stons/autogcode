package {{className}}



/**
 * @author: wangzhengtao
 * 2018年03月03日11:49:08
 */
interface DutyRepository : JpaRepository<{{className}}, Long>, QuerydslPredicateExecutor<{{className}}> {

}

private var d = Q{{className}}.{{lowercaseClassName}}
fun DutyRepository.toPredicate(c: {{className}}Condition) = com.querydsl.core.BooleanBuilder().apply {

}