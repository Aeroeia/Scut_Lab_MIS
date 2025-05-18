package com.mis.backend.service;

import com.mis.backend.vo.DashboardVO;
import com.mis.backend.vo.StatisticStudentVO;

public interface StatisticService {
    DashboardVO getDashboard();

    StatisticStudentVO getStudent(String studentId, Integer academicYear);
}
