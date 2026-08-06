package com.arcmethod.community.domain;
import com.arcmethod.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "community_post")
public class CommunityPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    @Column(name = "board_type", nullable = false, length = 20)
    private String boardType;
    @Column(nullable =false, length = 200)
    private String title;
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    @Column(name = "view_count", nullable = false)
    private int viewCount;
    public void increaseView(){
        this.viewCount++;
    }
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
    public static CommunityPost create(Long memberId, String boardType, String title, String content){
        CommunityPost p = new CommunityPost();
        p.memberId = memberId;
        p.boardType = boardType;
        p.title = title;
        p.content = content;
        return p;
    }
}
