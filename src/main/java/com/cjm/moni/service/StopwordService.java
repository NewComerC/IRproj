package com.cjm.moni.service;

import com.cjm.moni.entity.Stopword;
import com.baomidou.mybatisplus.service.IService;
import java.util.List;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agp
 * @since 2018-11-30
 */
public interface StopwordService extends IService<Stopword> {

    List<String> getStopWords();
}
