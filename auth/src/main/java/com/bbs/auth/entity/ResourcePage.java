package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourcePage {

    private Long id;

    private String name;

    private String path;

    /**
     * 组件路径
     */
    private String componentPath;

    private Long parentId;

    /**
     * 状态（0正常、1不可用）
     */
    private Integer state;

    /**
     * 层级（0起始，数字越大越底层）
     */
    private Integer hierarchical;

    /**
     * 子级
     */
    @TableField(exist = false)
    private List<ResourcePage> children;
}
