package com.issuemoa.batch.service;

import com.issuemoa.batch.domain.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
}
