package com.hawk.mybatis.batch;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-21 10:17
 */
public class BatchInsertMethod extends AbstractMethod {
    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    protected BatchInsertMethod(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>\nINSERT INTO %s %s \nVALUES %s\n</script>";
        sql = String.format(sql, tableInfo.getTableName(),
                getFieldSql(tableInfo), getValueSql(tableInfo));
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass,
                "insertBatch", sqlSource, new NoKeyGenerator(),
                tableInfo.getKeyProperty(), tableInfo.getKeyColumn());
    }

    private String getFieldSql(TableInfo tableInfo) {
        return "(" + tableInfo.getAllSqlSelect() + ")";
    }

    private String getValueSql(TableInfo tableInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("<foreach collection=\"dataList\" item=\"data\" open=\"(\" close=\")\"" +
                " index=\"index\" separator=\"),(\">\n");
        sb.append("#{data.").append(tableInfo.getKeyProperty()).append("},");
        AtomicBoolean isNotFirst = new AtomicBoolean(false);
        tableInfo.getFieldList().forEach(field -> {
            if (isNotFirst.get()) {
                sb.append(",");
            }
            sb.append("#{data.").append(field.getProperty()).append("}");
            isNotFirst.set(true);
        });
        sb.append("\n</foreach>");
        return sb.toString();
    }
}
