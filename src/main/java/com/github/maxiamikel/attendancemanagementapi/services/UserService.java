package com.github.maxiamikel.attendancemanagementapi.services;

import com.github.maxiamikel.attendancemanagementapi.dto.request.UserRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.User;

public interface UserService {

    public User createAccount(UserRequest request);
}
