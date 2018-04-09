package templates

import org.appsugar.archetypes.bus.entity.ExperienceLearnInfo
import org.appsugar.archetypes.bus.entity.LearnBooking
import org.appsugar.archetypes.bus.repository.ExperienceLearnInfoRepository
import org.appsugar.archetypes.bus.repository.LearnBookingRepository
import org.appsugar.archetypes.bus.repository.PrepareInfoRepository
import org.appsugar.archetypes.common.dto.Response
import org.appsugar.archetypes.common.entity.StudentBaseCardInfo
import org.appsugar.archetypes.common.enums.BookingStatus
import org.appsugar.archetypes.common.enums.IsFlag
import org.appsugar.archetypes.common.enums.LearnPlan
import org.appsugar.archetypes.extension.*
import org.appsugar.archetypes.res.entity.School
import org.appsugar.archetypes.sys.entity.User
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * @author: wangzhengtao
 * 2018年03月20日11:03:55
 */
@Service
@Transactional
class `projectName_groupName_module_service_impl_ClassNameService.kt`(
        var bookRepository: LearnBookingRepository,
        var eliRepository: ExperienceLearnInfoRepository,
        var prepareRepository: PrepareInfoRepository
) {
    private companion object {
        val logger: Logger by lazy { getLogger<`projectName_groupName_module_service_impl_ClassNameService.kt`>() }
    }

    @Autowired
    lateinit var prepareinfoService: PrepareInfoService

    /**
     * @param orderTime 预约时间
     * @param id 试学信息id
     */
    fun booking(orderTime: LocalDateTime, id: Long, user: User): Response<LearnBooking> {
        val eli = eliRepository.findById(id).get()
        var book = LearnBooking()
        book.experienceLearnInfo = eli
        book.optionUser = user
        book.orderTime = orderTime
        book.school = eli.school
        book.learnCode = generateLearnCode(orderTime, eli.school!!.id)
        //save booking
        bookRepository.save(book)
        //update eli
        eliRepository.updateBookingStatus(id, BookingStatus.BOOKED)

        return Response(message = "预约成功", data = book.copy())
    }

    private fun generateLearnCode(orderTime: LocalDateTime, id: Long): String {
        //根据驾校和预约时间查询最新的编号
        val lastCode = bookRepository.findLastLearnCode(orderTime.toStartDateTime(), orderTime.toEndDateTime(), id) ?: "000"
        return String.format("%03d", lastCode.toInt() + 1)
    }

    /**
     * 试学登记
     * 根据手机号查询试学信息，如果存在，修改状态未已试学 试学报名也更改为已试学
     * 如果不存在，保存一条试学信息
     */
    fun record(baseInfo: StudentBaseCardInfo, learnPlan: LearnPlan, schoolId: Long): LearnBooking? {
        val phone = baseInfo.phone
        val eli = eliRepository.findOneByPhoneAndBookStatus(phone, BookingStatus.BOOKED, schoolId)
        if (eli.isNotNull()) {
            eli!!.learnPlan = learnPlan
            eli.bookingStatus = BookingStatus.LEARNED
            val book = bookRepository.findByEliIdAndOrderTime(eli.id, LocalDateTime.now().toStartDateTime())
            if (book.isNull()) return LearnBooking()
            book!!.recordTime = LocalDateTime.now()
            bookRepository.saveAndFlush(book)
            eliRepository.saveAndFlush(eli)
            //TODO 试学报名修改为已试学]
            if (eli.prepareInfo?.id != null) {
                prepareinfoService.updateIsTest(eli.prepareInfo!!.id, IsFlag.YES)
            }

            return book
        } else {
            var newEli = ExperienceLearnInfo(bookingStatus = BookingStatus.LEARNED, learnPlan = learnPlan)
            var school = School()
            school.id = schoolId
            newEli.studentBaseCardInfo = baseInfo
            newEli.school = school
            eliRepository.save(newEli)
            var book = LearnBooking()
            book.experienceLearnInfo = newEli
            book.recordTime = LocalDateTime.now()
            book.school = school
            book.learnCode = generateLearnCode(LocalDateTime.now(), schoolId)
            bookRepository.saveAndFlush(book)
            return book
        }

    }

    /**
     * 退回跟踪
     */
    fun back(id: Long) {
        eliRepository.updateBookingStatus(id, BookingStatus.BACK)
        val eli = eliRepository.findById(id).get()
        prepareRepository.updateShowFlag(eli.prepareInfo!!.id, IsFlag.YES)
    }

}



