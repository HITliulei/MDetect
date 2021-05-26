package com.ll.centralcontrol.mapper;

import com.ll.centralcontrol.dao.MServiceDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author SeptemberHX
 * @version 0.1
 * @date 2019/12/20
 */
public interface ServicesMapper {
    @Select("SELECT * FROM services")
    @Results({
            @Result(property = "serviceId", column = "id"),
            @Result(property = "serviceName", column = "name"),
            @Result(property = "serviceVersion", column = "version"),
            @Result(property = "serviceImage", column = "image"),
            @Result(property = "port", column = "port"),
            @Result(property = "basePath", column = "basePath")
    })
    List<MServiceDao> getAll();

    @Select("SELECT * FROM services WHERE id = #{serviceId}")
    @Results({
            @Result(property = "serviceId", column = "id"),
            @Result(property = "serviceName", column = "name"),
            @Result(property = "serviceVersion", column = "version"),
            @Result(property = "serviceImage", column = "image"),
            @Result(property = "port", column = "port"),
            @Result(property = "basePath", column = "basePath")
    })
    MServiceDao getById(String serviceId);

    @Insert("INSERT INTO services(id, name, version, image, port, basePath)" +
            " VALUES(#{serviceId}, #{serviceName}, #{serviceVersion}, #{serviceImage}, #{port}, #{basePath})")
    void insert(MServiceDao serviceDao);

    @Select("SELECT * FROM services WHERE name = #{serviceName}")
    @Results({
            @Result(property = "serviceId", column = "id"),
            @Result(property = "serviceName", column = "name"),
            @Result(property = "serviceVersion", column = "version"),
            @Result(property = "serviceImage", column = "image"),
            @Result(property = "port", column = "port"),
            @Result(property = "basePath", column = "basePath")
    })
    List<MServiceDao> getByName(String serviceName);

    @Delete("DELETE FROM services WHERE id = #{serviceId}")
    void deleteById(String serviceId);

    @Update("UPDATE services SET name = #{serviceName}, version = #{serviceVersion}, image = #{serviceImage}, port = #{port}, basePath = #{basePath} WHERE id = #{serviceId}")
    void update(MServiceDao serviceDao);

    @Update("UPDATE services SET image = #{imageUrl} WHERE id = #{serviceId}")
    void updateImageUrl(@Param("serviceId") String serviceId, @Param("imageUrl") String imageUrl);
}
