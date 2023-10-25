package com.xixi.bi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xixi.bi.model.dto.post.PostQueryRequest;
import com.xixi.bi.model.entity.Post;
import javax.annotation.Resource;

import com.xixi.bi.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 帖子服务测试
 *
 *
 */
@SpringBootTest
class PostServiceTest {

    @Resource
    private PostService postService;

    @Test
    void searchFromEs() {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setUserId(1L);
        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
        Assertions.assertNotNull(postPage);
    }

}