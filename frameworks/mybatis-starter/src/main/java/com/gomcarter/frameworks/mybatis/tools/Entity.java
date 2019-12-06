package com.gomcarter.frameworks.mybatis.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gomcarter on 2019-12-05 22:47:06
 */
public class Entity {
    public String poundSign = "#";
    public String dollarSign = "$";
    public String basePackageName;
    public String entityPackageName;
    public String dtoPackageName;
    public String paramPackage;
    public String abstractEntityPackageName;
    public String daoPackageName;
    public String servicePackageName;
    public String actionPackageName;
    public String className;
    public String classInstanceName;
    public String tableName;
    public String cloumnName;
    public String idName;
    public String idColumn;
    public String idType;
    public String idSimpleType;
    public String idGetMethod;
    public String idSetMethod;
    public boolean idAutoIncrement = true;
    public boolean hasDateType = false;
    public boolean hasDecimalType = false;
    public boolean hasSetType = false;
    public boolean manyToMany = false;
    public boolean hasHibernateVersion = false;
    public boolean hasFetchType = false;
    public boolean hasJoinColumn = false;
    public String author;
    public String createTime;
    public String versionLock;
    public List<Property> propList = new ArrayList<Property>();
    public List<SetProperty> sPropList = new ArrayList<SetProperty>();
    public List<AliasProperty> aPropList = new ArrayList<AliasProperty>();

    public String getDollarSign() {
        return dollarSign;
    }

    public Entity setDollarSign(String dollarSign) {
        this.dollarSign = dollarSign;
        return this;
    }

    public String getParamPackage() {
        return paramPackage;
    }

    public Entity setParamPackage(String paramPackage) {
        this.paramPackage = paramPackage;
        return this;
    }

    public String getVersionLock() {
        return versionLock;
    }

    public void setVersionLock(String versionLock) {
        this.versionLock = versionLock;
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public String getDtoPackageName() {
        return dtoPackageName;
    }

    public void setDtoPackageName(String dtoPackageName) {
        this.dtoPackageName = dtoPackageName;
    }

    public String getAbstractEntityPackageName() {
        return abstractEntityPackageName;
    }

    public void setAbstractEntityPackageName(String abstractEntityPackageName) {
        this.abstractEntityPackageName = abstractEntityPackageName;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    public String getActionPackageName() {
        return actionPackageName;
    }

    public void setActionPackageName(String actionPackageName) {
        this.actionPackageName = actionPackageName;
    }

    public String getClassInstanceName() {
        return classInstanceName;
    }

    public void setClassInstanceName(String classInstanceName) {
        this.classInstanceName = classInstanceName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getDaoPackageName() {
        return daoPackageName;
    }

    public void setDaoPackageName(String daoPackageName) {
        this.daoPackageName = daoPackageName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdSimpleType() {
        return idSimpleType;
    }

    public void setIdSimpleType(String idSimpleType) {
        this.idSimpleType = idSimpleType;
    }

    public String getIdGetMethod() {
        return idGetMethod;
    }

    public void setIdGetMethod(String idGetMethod) {
        this.idGetMethod = idGetMethod;
    }

    public String getIdSetMethod() {
        return idSetMethod;
    }

    public void setIdSetMethod(String idSetMethod) {
        this.idSetMethod = idSetMethod;
    }

    public boolean isIdAutoIncrement() {
        return idAutoIncrement;
    }

    public void setIdAutoIncrement(boolean idAutoIncrement) {
        this.idAutoIncrement = idAutoIncrement;
    }

    public boolean isHasDateType() {
        return hasDateType;
    }

    public void setHasDateType(boolean hasDateType) {
        this.hasDateType = hasDateType;
    }

    public boolean isHasDecimalType() {
        return hasDecimalType;
    }

    public void setHasDecimalType(boolean hasDecimalType) {
        this.hasDecimalType = hasDecimalType;
    }

    public boolean isHasSetType() {
        return hasSetType;
    }

    public void setHasSetType(boolean hasSetType) {
        this.hasSetType = hasSetType;
    }

    public boolean isManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(boolean manyToMany) {
        this.manyToMany = manyToMany;
    }

    public boolean isHasHibernateVersion() {
        return hasHibernateVersion;
    }

    public void setHasHibernateVersion(boolean hasHibernateVersion) {
        this.hasHibernateVersion = hasHibernateVersion;
    }

    public List<Property> getPropList() {
        return propList;
    }

    public void setPropList(List<Property> propList) {
        this.propList = propList;
    }

    public List<SetProperty> getsPropList() {
        return sPropList;
    }

    public void setsPropList(List<SetProperty> sPropList) {
        this.sPropList = sPropList;
    }

    public List<AliasProperty> getaPropList() {
        return aPropList;
    }

    public void setaPropList(List<AliasProperty> aPropList) {
        this.aPropList = aPropList;
    }


    public static class Property {
        public String propName;
        public String column;
        public String propType;
        public String notAllowNull;
        public String note;
        public String simpleType;
        public String getMethod;
        public String setMethod;
        public boolean manyToOne = false;

        public boolean isManyToOne() {
            return manyToOne;
        }

        public void setManyToOne(boolean manyToOne) {
            this.manyToOne = manyToOne;
        }

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getPropType() {
            return propType;
        }

        public void setPropType(String propType) {
            this.propType = propType;
        }

        public String getNotAllowNull() {
            return notAllowNull;
        }

        public void setNotAllowNull(String notAllowNull) {
            this.notAllowNull = notAllowNull;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getSimpleType() {
            return simpleType;
        }

        public void setSimpleType(String simpleType) {
            this.simpleType = simpleType;
        }

        public String getGetMethod() {
            return getMethod;
        }

        public void setGetMethod(String getMethod) {
            this.getMethod = getMethod;
        }

        public String getSetMethod() {
            return setMethod;
        }

        public void setSetMethod(String setMethod) {
            this.setMethod = setMethod;
        }
    }

    /**
     * 一对多/多对多关系
     */
    public static class SetProperty {
        public String foreignTableName;
        public String foreignClassName;
        public String foreignColumnName;
        public String manyForeignColumnName;
        public String propName;
        public String getMethod;
        public String setMethod;
        public boolean manyToMany = false;
        public String manyToManyTable;

        public String getManyForeignColumnName() {
            return manyForeignColumnName;
        }

        public void setManyForeignColumnName(String manyForeignColumnName) {
            this.manyForeignColumnName = manyForeignColumnName;
        }

        public String getForeignTableName() {
            return foreignTableName;
        }

        public void setForeignTableName(String foreignTableName) {
            this.foreignTableName = foreignTableName;
        }

        public String getForeignClassName() {
            return foreignClassName;
        }

        public void setForeignClassName(String foreignClassName) {
            this.foreignClassName = foreignClassName;
        }

        public String getForeignColumnName() {
            return foreignColumnName;
        }

        public void setForeignColumnName(String foreignColumnName) {
            this.foreignColumnName = foreignColumnName;
        }

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }

        public String getGetMethod() {
            return getMethod;
        }

        public void setGetMethod(String getMethod) {
            this.getMethod = getMethod;
        }

        public String getSetMethod() {
            return setMethod;
        }

        public void setSetMethod(String setMethod) {
            this.setMethod = setMethod;
        }

        public boolean isManyToMany() {
            return manyToMany;
        }

        public void setManyToMany(boolean manyToMany) {
            this.manyToMany = manyToMany;
        }

        public String getManyToManyTable() {
            return manyToManyTable;
        }

        public void setManyToManyTable(String manyToManyTable) {
            this.manyToManyTable = manyToManyTable;
        }
    }

    /**
     * 多对一关系
     */
    public static class AliasProperty {
        public String primaryTableName;
        public String primaryClassName;
        public String columnName;
        public String propName;
        public String getMethod;
        public String setMethod;

        public String getPrimaryTableName() {
            return primaryTableName;
        }

        public void setPrimaryTableName(String primaryTableName) {
            this.primaryTableName = primaryTableName;
        }

        public String getPrimaryClassName() {
            return primaryClassName;
        }

        public void setPrimaryClassName(String primaryClassName) {
            this.primaryClassName = primaryClassName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }

        public String getGetMethod() {
            return getMethod;
        }

        public void setGetMethod(String getMethod) {
            this.getMethod = getMethod;
        }

        public String getSetMethod() {
            return setMethod;
        }

        public void setSetMethod(String setMethod) {
            this.setMethod = setMethod;
        }
    }

    public boolean isHasFetchType() {
        return hasFetchType;
    }

    public void setHasFetchType(boolean hasFetchType) {
        this.hasFetchType = hasFetchType;
    }

    public boolean isHasJoinColumn() {
        return hasJoinColumn;
    }

    public void setHasJoinColumn(boolean hasJoinColumn) {
        this.hasJoinColumn = hasJoinColumn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCloumnName() {
        return cloumnName;
    }

    public void setCloumnName(String cloumnName) {
        this.cloumnName = cloumnName;
    }

    public String getPoundSign() {
        return poundSign;
    }

    public void setPoundSign(String poundSign) {
        this.poundSign = poundSign;
    }
}
