package com.todd.toj_backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.pojo.problem.ProblemConfig;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@MappedTypes({ProblemConfig.class})
public class JsonTypeHandler extends BaseTypeHandler<ProblemConfig>  {

    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ProblemConfig config, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setObject(i, objectMapper.writeValueAsString(config));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProblemConfig getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if(resultSet.getString(s) != null){
            try {
                return objectMapper.readValue(resultSet.getString(s), ProblemConfig.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ProblemConfig getNullableResult(ResultSet resultSet, int i) throws SQLException {
        if (resultSet.getString(i) != null) {
            try {
                return objectMapper.readValue(resultSet.getString(i), ProblemConfig.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ProblemConfig getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        if(callableStatement.getString(i) != null){
            try {
                return objectMapper.readValue(callableStatement.getString(i), ProblemConfig.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}