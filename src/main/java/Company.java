import java.math.BigDecimal;

public class Company
{
   private String name;
   private BigDecimal assets;
   private BigDecimal liabilities;
   private BigDecimal equities;
   private BigDecimal incomes;

   public Company( String name )
   {
      this.name = name;
      assets = BigDecimal.ZERO;
      liabilities = BigDecimal.ZERO;
      equities = BigDecimal.ZERO;
      incomes = BigDecimal.ZERO;
   }

   public void addAsset( BigDecimal asset )
   {
      assets = assets.add( asset );
   }

   public void addLiability( BigDecimal liability )
   {
      liabilities = liabilities.add( liability );
   }

   public void addEquity( BigDecimal equity )
   {
      equities = equities.add( equity );
   }

   public void addIncome( BigDecimal income )
   {
      incomes = incomes.add( income );
   }

   public BigDecimal getTotal()
   {
      return getAssets().add( getLiabilities() ).add( getEquities() ).add( getIncomes() );
   }

   public String getName()
   {
      return name;
   }

   public BigDecimal getAssets()
   {
      return assets;
   }

   public BigDecimal getLiabilities()
   {
      return liabilities;
   }

   public BigDecimal getEquities()
   {
      return equities;
   }

   public BigDecimal getIncomes()
   {
      return incomes;
   }
}
