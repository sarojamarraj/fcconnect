package com.freightcom.api.repositories.custom;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freightcom.api.ReportableError;

public class BaseSpecification
{
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    protected final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    protected static String SPLIT_PATTERN = "\\s*,\\s*";

    public BaseSpecification()
    {

    }

    public boolean yesValueX(String value)
    {
        return value != null
                && (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"));
    }

    public boolean yesValue(Object value)
    {
        return value != null && (((value instanceof Boolean) && ((Boolean) value).booleanValue())
                || (value instanceof String && ((((String) value).equalsIgnoreCase("yes")
                        || ((String) value).equalsIgnoreCase("true") || ((String) value).equalsIgnoreCase("1")))));
    }

    public void build(Predicate predicate, Map<String, ?> criteria, Root<?> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
    }

    public String likePattern(Map.Entry<String, Object> entry)
    {
        return "%" + entry.getValue()
                .toString()
                .replace("%", "\\%") + "%";
    }

    public String likePattern(Object value)
    {
        return "%" + value.toString()
                .replace("%", "\\%") + "%";
    }

    public void addString(Predicate predicate, String column, Object value, Root<?> root, CriteriaBuilder builder)
    {
        if (value == null) {
            predicate.getExpressions()
                    .add(builder.isNull(root.get(column)));
        } else {
            predicate.getExpressions()
                    .add(builder.equal(root.get(column), value));
        }
    }

    public void addLike(Predicate predicate, String column, Object value, Root<?> root, CriteriaBuilder builder)
    {
        if (value == null) {
            predicate.getExpressions()
                    .add(builder.isNull(root.get(column)));
        } else {
            predicate.getExpressions()
                    .add(builder.like(root.get(column), likePattern(value)));
        }
    }

    public void addLike(Predicate predicate, Expression<String> path, Object value, Root<?> root,
            CriteriaBuilder builder)
    {
        if (value == null) {
            predicate.getExpressions()
                    .add(builder.isNull(path));
        } else {
            predicate.getExpressions()
                    .add(builder.like(path, likePattern(value)));
        }
    }

    public Predicate addLike(Expression<String> path, Object value, CriteriaBuilder builder)
    {
        if (value == null) {
            return builder.isNull(path);
        } else {
            return builder.like(path, likePattern(value));
        }
    }

    public List<Long> splitIDs(Object value)
    {
        // Split into list of long
        List<Long> values = new ArrayList<Long>();

        if (value == null) {
            values.add(null);
        } else {
            for (String id : ((String) value).split("\\s*,\\s*")) {
                values.add(Long.parseLong(id));
            }
        }

        return values;
    }

    /**
     *
     */
    public void addLong(Predicate predicate, String column, Object value, Root<?> root, CriteriaBuilder builder)
    {
        try {
            if (value == null) {
                predicate.getExpressions()
                        .add(builder.isNull(root.get(column)));
            } else if (value instanceof String && ((String) value).matches("^.*,.*$")) {
                // Split into list of long, do in test
                List<Long> values = new ArrayList<Long>();

                for (String id : ((String) value).split("\\s*,\\s*")) {
                    values.add(Long.parseLong(id));
                }

                predicate.getExpressions()
                        .add(builder.in(root.get(column))
                                .value(values));
            } else {
                Long number = value instanceof Long ? (Long) value : Long.parseLong(value.toString());
                predicate.getExpressions()
                        .add(builder.equal(root.get(column), number));
            }
        } catch (NumberFormatException e) {
            throw new ReportableError(column + " " + e.getMessage());
        }
    }

    public void addLongId(Predicate predicate, String column, Object value, Root<?> root, CriteriaBuilder builder)
    {
        try {
            if (value == null) {
                predicate.getExpressions()
                        .add(builder.isNull(root.get(column)));
            } else {
                Long number = value instanceof Long ? (Long) value : Long.parseLong(value.toString());
                log.debug("BASE SPEC ADD NUMBER " + column + " = " + value + " " + number);
                predicate.getExpressions()
                        .add(builder.equal(root.get(column)
                                .get("id"), number));
            }
        } catch (NumberFormatException e) {
            throw new ReportableError(column + " " + e.getMessage());
        }
    }

    public void addNumericRange(Predicate predicate, String column, Object value, Root<?> root, CriteriaBuilder builder)
    {
        if (value == null) {
            predicate.getExpressions()
                    .add(builder.isNull(root.get(column)));
        } else {
            BigDecimal number = new BigDecimal(value.toString());

            predicate.getExpressions()
                    .add(builder.lessThanOrEqualTo(root.get(column), number.add(new BigDecimal(1))));
            predicate.getExpressions()
                    .add(builder.greaterThanOrEqualTo(root.get(column), number.subtract(new BigDecimal(1))));
        }
    }

    public void dateLessThan(Predicate predicate, Expression<Date> path, Object value, Root<?> root,
            CriteriaBuilder builder)
    {
        if (value == null) {
            predicate.getExpressions()
                    .add(builder.isNull(path));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = ZonedDateTime.now(ZoneId.of("UTC"))
                    .truncatedTo(ChronoUnit.DAYS)
                    .minusDays(Integer.parseInt(value.toString()))
                    .format(formatter);

            predicate.getExpressions()
                    .add(builder.lessThanOrEqualTo(builder.function("DATE", Date.class, path),
                            builder.function("DATE", Date.class, builder.literal(date))));
        }
    }

    public void addDate(Predicate predicate, String column, Object value, Root<?> root, CriteriaBuilder builder)
    {
        addDate(predicate, root.get(column), value, root, builder);
    }

    public void addDate(Predicate predicate, Expression<Date> path, Object value, Root<?> root, CriteriaBuilder builder)
    {
        if (value == null) {
            predicate.getExpressions()
                    .add(builder.isNull(path));
        } else {
            String pattern = value.toString();

            Date today = Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                    .truncatedTo(ChronoUnit.DAYS)
                    .with(LocalTime.of(0, 0, 0))
                    .toInstant());
            Date endToday = Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                    .truncatedTo(ChronoUnit.DAYS)
                    .with(LocalTime.of(23, 59, 59))
                    .toInstant());

            if (pattern.equalsIgnoreCase("custom")) {
                // ignore

            } else if (pattern.equalsIgnoreCase("upcoming")) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(path, builder.literal(today)));

            } else if (pattern.equalsIgnoreCase("past")) {
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(path, builder.literal(today)));

            } else if (pattern.equalsIgnoreCase("today")) {
                predicate.getExpressions()
                        .add(builder.between(path, builder.literal(today), builder.literal(endToday)));

            } else if (pattern.equalsIgnoreCase("tomorrow")) {
                predicate.getExpressions()
                        .add(builder.between(path, builder.literal(Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                                .plusDays(1)
                                .with(LocalTime.of(0, 0, 0))
                                .toInstant())),
                                builder.literal(Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                                        .plusDays(1)
                                        .with(LocalTime.of(23, 59, 59))
                                        .toInstant()))));

            } else if (pattern.matches("^\\d\\d\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.equal(builder.function("YEAR", Integer.class, path),
                                builder.literal(Integer.parseInt(pattern, 10))));

            } else if (pattern.matches("^\\d\\d\\d\\d-\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.equal(
                                builder.function("DATE_FORMAT", String.class, path, builder.literal("%Y-%m")),
                                builder.literal(pattern)));

            } else if (pattern.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.equal(builder.function("DATE", String.class, path),
                                builder.function("DATE", String.class, builder.literal(pattern))));

            } else {
                throw new ReportableError("Bad date " + value);
            }
        }
    }

    public void addDateFrom(Predicate predicate, String column, Object value, Root<?> root, CriteriaBuilder builder)
    {
        if (value == null) {
            predicate.getExpressions()
                    .add(builder.isNull(root.get(column)));
        } else {
            String pattern = value.toString();

            if (pattern.matches("^\\d\\d\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(builder.function("YEAR", Integer.class, root.get(column)),
                                builder.literal(Integer.parseInt(pattern, 10))));

            } else if (pattern.matches("^\\d\\d\\d\\d-\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(builder.function("DATE_FORMAT", String.class,
                                root.get(column), builder.literal("%Y-%m")), builder.literal(pattern)));

            } else if (pattern.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(builder.function("DATE", String.class, root.get(column)),
                                builder.function("DATE", String.class, builder.literal(pattern))));

            } else {
                throw new ReportableError("Bad date " + value);
            }
        }
    }

    public void addDateTo(Predicate predicate, String column, Object value, Root<?> root, CriteriaBuilder builder)
    {
        if (value == null) {
            predicate.getExpressions()
                    .add(builder.isNull(root.get(column)));
        } else {
            String pattern = value.toString();

            if (pattern.matches("^\\d\\d\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(builder.function("YEAR", Integer.class, root.get(column)),
                                builder.literal(Integer.parseInt(pattern, 10))));

            } else if (pattern.matches("^\\d\\d\\d\\d-\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(builder.function("DATE_FORMAT", String.class, root.get(column),
                                builder.literal("%Y-%m")), builder.literal(pattern)));

            } else if (pattern.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d$")) {
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(builder.function("DATE", String.class, root.get(column)),
                                builder.function("DATE", String.class, builder.literal(pattern))));

            } else {
                throw new ReportableError("Bad date " + value);
            }
        }
    }

    public Long asLong(Object value)
    {
        if (value == null) {
            return null;
        } else if (value instanceof Long) {
            return (Long) value;
        } else {
            try {
                return Long.parseLong(value.toString());
            } catch (Throwable e) {
                throw new ReportableError("Invalid id parameter: " + value);
            }
        }
    }
}
