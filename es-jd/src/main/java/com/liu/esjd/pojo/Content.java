package com.liu.esjd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @author LiuYi
 * @since 2022/6/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    private String title;

    private String img;

    private String price;

    private String shop;
}
