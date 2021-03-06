package tech.tablesaw.reducing;

import org.junit.Before;
import org.junit.Test;

import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.reducing.NumericReduceUtils;
import tech.tablesaw.table.ViewGroup;

import static org.junit.Assert.assertEquals;


public class NumericReduceUtilsTest {

    private static ColumnType[] types = {
            ColumnType.LOCAL_DATE,     // date of poll
            ColumnType.INTEGER,        // approval rating (pct)
            ColumnType.CATEGORY        // polling org
    };

    private Table table;

    @Before
    public void setUp() throws Exception {
        table = Table.read().csv(CsvReadOptions.builder("../data/BushApproval.csv").columnTypes(types));
    }

    @Test
    public void testMean() {
        double result = table.reduce("approval", NumericReduceUtils.mean);
        assertEquals(64.88235294117646, result, 0.01);
    }

    @Test
    public void testGroupMean() {
        Column byColumn = table.column("who");
        ViewGroup group = new ViewGroup(table, byColumn);
        Table result = group.reduce("approval", NumericReduceUtils.mean);
        assertEquals(2, result.columnCount());
        assertEquals("who", result.column(0).name());
        assertEquals(6, result.rowCount());
        assertEquals("65.671875", result.get(0, 1));
    }

    @Test
    public void test2ColumnGroupMean() {
        Column byColumn1 = table.column("who");
        ViewGroup group = new ViewGroup(table, byColumn1);
        Table result = group.reduce("approval", NumericReduceUtils.mean);
        assertEquals(2, result.columnCount());
        assertEquals("who", result.column(0).name());
        assertEquals(6, result.rowCount());
        assertEquals("65.671875", result.get(0, 1));
    }
}