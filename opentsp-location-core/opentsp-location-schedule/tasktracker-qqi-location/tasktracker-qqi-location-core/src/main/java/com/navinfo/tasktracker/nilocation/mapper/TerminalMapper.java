package com.navinfo.tasktracker.nilocation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
public interface TerminalMapper {

    List<Long> selectTerminals();

    void insertTerminal(Long TERMINAL_ID);
}




