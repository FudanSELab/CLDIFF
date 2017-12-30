package edu.fdu.se.bean;

import java.util.ArrayList;
import java.util.List;

public class AndroidCacheCommitExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public AndroidCacheCommitExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andIdIsNull() {
			addCriterion("id is null");
			return (Criteria) this;
		}

		public Criteria andIdIsNotNull() {
			addCriterion("id is not null");
			return (Criteria) this;
		}

		public Criteria andIdEqualTo(Integer value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(Integer value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(Integer value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(Integer value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(Integer value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<Integer> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<Integer> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(Integer value1, Integer value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(Integer value1, Integer value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andKeyEntryIsNull() {
			addCriterion("key_entry is null");
			return (Criteria) this;
		}

		public Criteria andKeyEntryIsNotNull() {
			addCriterion("key_entry is not null");
			return (Criteria) this;
		}

		public Criteria andKeyEntryEqualTo(String value) {
			addCriterion("key_entry =", value, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryNotEqualTo(String value) {
			addCriterion("key_entry <>", value, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryGreaterThan(String value) {
			addCriterion("key_entry >", value, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryGreaterThanOrEqualTo(String value) {
			addCriterion("key_entry >=", value, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryLessThan(String value) {
			addCriterion("key_entry <", value, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryLessThanOrEqualTo(String value) {
			addCriterion("key_entry <=", value, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryLike(String value) {
			addCriterion("key_entry like", value, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryNotLike(String value) {
			addCriterion("key_entry not like", value, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryIn(List<String> values) {
			addCriterion("key_entry in", values, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryNotIn(List<String> values) {
			addCriterion("key_entry not in", values, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryBetween(String value1, String value2) {
			addCriterion("key_entry between", value1, value2, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andKeyEntryNotBetween(String value1, String value2) {
			addCriterion("key_entry not between", value1, value2, "keyEntry");
			return (Criteria) this;
		}

		public Criteria andCommitIdIsNull() {
			addCriterion("commit_id is null");
			return (Criteria) this;
		}

		public Criteria andCommitIdIsNotNull() {
			addCriterion("commit_id is not null");
			return (Criteria) this;
		}

		public Criteria andCommitIdEqualTo(String value) {
			addCriterion("commit_id =", value, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdNotEqualTo(String value) {
			addCriterion("commit_id <>", value, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdGreaterThan(String value) {
			addCriterion("commit_id >", value, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdGreaterThanOrEqualTo(String value) {
			addCriterion("commit_id >=", value, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdLessThan(String value) {
			addCriterion("commit_id <", value, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdLessThanOrEqualTo(String value) {
			addCriterion("commit_id <=", value, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdLike(String value) {
			addCriterion("commit_id like", value, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdNotLike(String value) {
			addCriterion("commit_id not like", value, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdIn(List<String> values) {
			addCriterion("commit_id in", values, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdNotIn(List<String> values) {
			addCriterion("commit_id not in", values, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdBetween(String value1, String value2) {
			addCriterion("commit_id between", value1, value2, "commitId");
			return (Criteria) this;
		}

		public Criteria andCommitIdNotBetween(String value1, String value2) {
			addCriterion("commit_id not between", value1, value2, "commitId");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table android_cache_commit
     *
     * @mbg.generated do_not_delete_during_merge Sat Dec 30 14:47:32 CST 2017
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}