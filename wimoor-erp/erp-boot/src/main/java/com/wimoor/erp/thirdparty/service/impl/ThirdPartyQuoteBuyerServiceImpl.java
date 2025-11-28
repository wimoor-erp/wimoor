package com.wimoor.erp.thirdparty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyQuoteBuyerMapper;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyQuoteBuyer;
import com.wimoor.erp.thirdparty.service.IThirdPartyQuoteBuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("thirdPartyQuoteBuyerService")
@RequiredArgsConstructor
public class ThirdPartyQuoteBuyerServiceImpl extends ServiceImpl<ThirdPartyQuoteBuyerMapper, ThirdPartyQuoteBuyer> implements IThirdPartyQuoteBuyerService {

    @Override
    public ThirdPartyQuoteBuyer getQuoteToken(String shopid) {
        LambdaQueryWrapper<ThirdPartyQuoteBuyer> query=new LambdaQueryWrapper<ThirdPartyQuoteBuyer>();
        query.eq(ThirdPartyQuoteBuyer::getShopid,shopid);
        return this.baseMapper.selectOne(query);
    }

    @Override
    public Boolean  removeQuoteToken(String shopid) {
        LambdaQueryWrapper<ThirdPartyQuoteBuyer> query=new LambdaQueryWrapper<>();
        query.eq(ThirdPartyQuoteBuyer::getShopid,shopid);
        return this.baseMapper.delete(query)>0;

    }

    @Override
    public ThirdPartyQuoteBuyer saveQuoteToken(ThirdPartyQuoteBuyer quote) {
        LambdaQueryWrapper<ThirdPartyQuoteBuyer> query=new LambdaQueryWrapper<>();
        query.eq(ThirdPartyQuoteBuyer::getShopid,quote.getShopid());
        ThirdPartyQuoteBuyer oldQuote = this.baseMapper.selectOne(query);
        if(quote.getIsowner()==null|| !quote.getIsowner()){
            List<ThirdPartyQuoteBuyer> list = this.baseMapper.selectList(new LambdaQueryWrapper<ThirdPartyQuoteBuyer>()
                                                                        .eq(ThirdPartyQuoteBuyer::getBuyertoken, quote.getBuyertoken())
                                                                        .eq(ThirdPartyQuoteBuyer::getIsowner,true));
            if(list==null|| list.isEmpty()){
                quote.setIsowner(true);
            }
        }
        if(oldQuote!=null){
            oldQuote.setBuyertoken(quote.getBuyertoken());
            this.baseMapper.updateById(oldQuote);
            return oldQuote;
        }else{
            this.baseMapper.insert(quote);
            return quote;
        }
    }

}
