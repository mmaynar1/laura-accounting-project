import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import java.io.FileInputStream;
import java.math.BigDecimal;

public class Laura
{
   public static final int COLUMN_M = 12;

   public void isCute()
   {
      try
      {
         System.out.println( System.getProperty( "user.dir" ) );
         Workbook workbook = WorkbookFactory.create( new FileInputStream( "laura.xlsx" ) );
         workbook.setForceFormulaRecalculation( true );
         for ( int i = 0; i < workbook.getNumberOfSheets(); i++ )
         {
            Sheet company = workbook.getSheetAt( i );
            System.out.println( company.toString() );
            BigDecimal assets = BigDecimal.ZERO;
            BigDecimal liabilities = BigDecimal.ZERO;
            BigDecimal equity = BigDecimal.ZERO;
            BigDecimal income = BigDecimal.ZERO;
            for ( Row row : company )
            {
               for ( Cell cell : row )
               {
                  CellReference cellReference = new CellReference( row.getRowNum(), cell.getColumnIndex() );
                  if ( cellReference.formatAsString().contains( "A" ) && cell.getCellType() == 0 )
                  {
                     //System.out.println(cellReference.formatAsString());
                     String accountNumber = (int) cell.getNumericCellValue() + "";

                     if ( AccountType.get( accountNumber ) == AccountType.Assets )
                     {
                        BigDecimal accountTotal = new BigDecimal( row.getCell( COLUMN_M ).getNumericCellValue() );
                        assets = assets.add( accountTotal );
                     }
                     else if ( AccountType.get( accountNumber ) == AccountType.Liabilities )
                     {
                        BigDecimal accountTotal = new BigDecimal( row.getCell( COLUMN_M ).getNumericCellValue() );
                        liabilities = liabilities.add( accountTotal );
                     }
                     else if ( AccountType.get( accountNumber ) == AccountType.Equity )
                     {
                        BigDecimal accountTotal = new BigDecimal( row.getCell( COLUMN_M ).getNumericCellValue() );
                        equity = equity.add( accountTotal );
                     }
                     else if ( AccountType.get( accountNumber ) == AccountType.Income )
                     {
                        BigDecimal accountTotal = new BigDecimal( row.getCell( COLUMN_M ).getNumericCellValue() );
                        System.out.println( row.getCell( COLUMN_M ).getNumericCellValue() );
                        System.out.println( accountTotal );
                        income = income.add( accountTotal );
                     }

                  }
               }
            }
            System.out.println( assets );
            System.out.println( liabilities );
            System.out.println( equity );
            System.out.println( income );
         }

      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
   }
}

