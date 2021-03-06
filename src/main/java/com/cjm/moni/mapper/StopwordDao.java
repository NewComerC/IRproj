package com.cjm.moni.mapper;

import com.cjm.moni.entity.Stopword;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author agp
 * @since 2018-11-30
 */
public interface StopwordDao extends BaseMapper<Stopword> {

    List<String> getStopWords();
}