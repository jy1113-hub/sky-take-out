package com.sky.service;

import com.sky.vo.*;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {

    /** 营业额统计 */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /** 用户统计 */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /** 订单统计 */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /** 查询销量排名Top10 */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /** 导出Excel报表 */
    void exportBusinessData(HttpServletResponse response);
}