package com.issuemoa.batch.domain.board;

import com.issuemoa.batch.domain.BaseTime;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "board")
public class Board extends BaseTime {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String type;
    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;
    @Column(name = "all_time_yn")
    private String allTimeYn;

    private String title;
    private String contents;
    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "read_cnt")
    private Long readCnt;

    @Column(name = "register_id")
    private Long registerId;

    @Column(name = "modify_id")
    private Long modifyId;

    public Board(String type, String allTimeYn, String title, String contents) {
        this.type = type;
        this.allTimeYn = allTimeYn;
        this.title = title;
        this.contents = contents;
    }
}