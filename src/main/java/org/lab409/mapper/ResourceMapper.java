package org.lab409.mapper;

import org.apache.ibatis.annotations.*;
import org.lab409.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResourceMapper {

    @Select("SELECT * FROM category")
    @Results({@Result(column = "id", property = "id"),
            @Result(column = "categoryName", property = "resourceCategoryName")})
    List<ResourceCategoryEntity> getResourceCategories();

    @Select("SELECT * FROM resourceMajor")
    List<ResourceMajorEntity> getResourceMajors();

    @Insert("INSERT IGNORE INTO resource (" +
            "resourceID, " +
            "uploaderID," +
            "categoryID, " +
            "resourceMajorID," +
            "resourceName," +
            "uploadTime," +
            "points, " +
            "description) VALUES (" +
            "#{resourceID}, " +
            "#{uploaderID}, " +
            "#{categoryID}, " +
            "#{resourceMajorID}," +
            "#{resourceName}," +
            "#{uploadTime}," +
            "#{points}," +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int uploadResource(ResourceEntity resourceEntity);

    @Update("UPDATE resource SET " +
            "categoryID = #{categoryID}, " +
            "resourceMajorID = #{resourceMajorID}," +
            "resourceName = #{resourceName}," +
            "description = #{description}," +
            "points = #{points} " +
            "WHERE resourceID = #{resourceID}")
    int updateResourceMetaData(ResourceEntity resourceEntity);

    @Delete("DELETE FROM resource WHERE resourceID= #{resourceID}")
    int deleteResource(@Param("resourceID") String resourceID);

    @Select("SELECT * FROM resource WHERE resourceID = #{resourceID} ")
    ResourceEntity getResourceFromID(@Param("resourceID") String resourceID);

    @Insert("INSERT IGNORE INTO favouriteResource(" +
            "id, " +
            "userID," +
            "resourceID) VALUES (" +
            "#{id}, " +
            "#{userID}," +
            "#{resourceID})")
    int insertIntoFavouriteResource(FavouriteResourceEntity favouriteResourceEntity);

    @Insert("INSERT INTO downloadResource(" +
            "id, " +
            "userID," +
            "resourceID) VALUES (" +
            "#{id}," +
            "#{userID}," +
            "#{resourceID})" +
            "ON DUPlICATE KEY UPDATE downloadTimes = downloadTimes + 1")
    int insertIntoDownloadResource(DownloadResourceEntity downloadResourceEntity);

    @Insert("INSERT IGNORE INTO suggestedResource(" +
            "id, " +
            "userID," +
            "resourceID) VALUES (" +
            "#{id}, " +
            "#{userID}," +
            "#{resourceID})")
    int insertIntoSuggestedResource(SuggestedResource favouriteResourceEntity);

    @Insert("INSERT INTO resourceComment(" +
            "userID," +
            "resourceID," +
            "comment," +
            "commentTime) VALUES (" +
            "#{userID}," +
            "#{resourceID}," +
            "#{comment}," +
            "#{commentTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertIntoResourceComment(ResourceComment resourceComment);

    @Update("UPDATE USER SET points = points + #{delta} WHERE ID = #{userID}")
    int updateLeftPoints(@Param("delta") Integer delta, @Param("userID") Integer userID);

    /*@Update("UPDATE downloadResource SET downloadTimes = downloadTimes+ 1 WHERE resourceID = #{resourceID} AND userID = #{userID}")
    int updateDownloadTimes(@Param("resourceID") String resourceID, @Param("userID") Integer userID);*/

    @Delete("DELETE FROM favouriteResource WHERE resourceID = #{resourceID} AND userID = #{userID}")
    int deleteFavouriteResource(@Param("resourceID") String resourceID, @Param("userID") Integer userID);

    @Delete("DELETE FROM suggestedResource WHERE resourceID = #{resourceID} AND userID = #{userID}")
    int deleteSuggestedResource(@Param("resourceID") String resourceID, @Param("userID") Integer userID);

    @Delete("DELETE FROM resourceComment WHERE id = #{commentID} AND userID = #{userID}")
    int deleteResourceComment(@Param("commentID") Integer commentID, @Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN favouriteResource f ON (r.resourceID = f.resourceID AND f.userID = #{userID} )")
    List<ResourceEntity> getFavouriteResourceList(@Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN downloadResource d ON (r.resourceID = d.resourceID AND d.userID = #{userID} )")
    List<ResourceEntity> getDownloadResourceList(@Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN suggestedResource s ON (r.resourceID = s.resourceID AND s.userID = #{userID} )")
    List<ResourceEntity> getSuggestedResourceList(@Param("userID") Integer userID);

    @Select("SELECT * FROM resourceComment WHERE resourceID = #{resourceID} ")
    List<ResourceComment> getResourceCommentList(@Param("resourceID") String resourceID);

    @Select("SELECT * FROM resourceComment WHERE userID = #{userID} ")
    List<ResourceComment> getMyResourceCommentList(@Param("userID") Integer userID);

    @Select("SELECT r.* FROM resource r INNER JOIN resourceComment c ON (r.resourceID = c.resourceID AND c.userID = #{userID} )")
    List<ResourceEntity> getCommentResourceList(@Param("userID") Integer userID);

    @Select("SELECT * FROM resource WHERE uploaderID = #{uploaderID} ")
    List<ResourceEntity> getUploadResourceList(@Param("uploaderID") Integer uploaderID);


}
