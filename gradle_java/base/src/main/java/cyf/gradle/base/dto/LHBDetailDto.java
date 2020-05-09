package cyf.gradle.base.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

/**
 * 龙虎榜详情
 *
 * @author Cheng Yufei
 * @create 2020-04-27 15:12
 **/
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LHBDetailDto implements Serializable {


    private static final long serialVersionUID = 5034690027002746892L;

    String stockCode;

    String stockName;

    /**
     * 收盘价
     */
    String closingPrice;

    /**
     * 涨跌幅
     */
    String pl;


    /**
     * 成交量： 万股
     */
    String volume;


    /**
     * 成交额： 万元
     */

    String volumeOfBusiness;

    /**
     * 换手率
     */

    String tr;
    /**
     * 振幅
     */

    String sl;

    /**
     * 量比
     */
    String cat;
    /**
     * 上榜类型
     */
    String type;

    /**
     * 买入金额最大的前5名，买入总计
     */
    String buySum;

    /**
     * 买入金额最大的前5名，买入总计占总成交比例
     */
    String buySumRatio;
    /**
     * 卖出金额最大的前5名，买入总计
     */
    String sellOfBuySum;

    /**
     * 卖出金额最大的前5名，买入总计占总成交比例
     */
    String sellOfBuySumRatio;

    /**
     * 买入金额最大的前5名
     */
    List<BranchInfo> buyBranchList;

    /**
     * 卖出金额最大的前5名
     */
    List<BranchInfo> sellBranchList;

    @Getter
    @Setter
   public class BranchInfo {
        /**
         * 营业厅名称
         */
        String branchName;

        /**
         * 营业厅买入金额
         */
        String bvalue;

        /**
         * 营业厅卖出金额
         */
        String svalue;

        /**
         * 营业厅净买入金额
         */
        String netvalue;

        public BranchInfo() {

        }

        public BranchInfo(String branchName, String bvalue, String svalue, String netvalue) {
            this.branchName = branchName;
            this.bvalue = bvalue;
            this.svalue = svalue;
            this.netvalue = netvalue;
        }
    }


}
