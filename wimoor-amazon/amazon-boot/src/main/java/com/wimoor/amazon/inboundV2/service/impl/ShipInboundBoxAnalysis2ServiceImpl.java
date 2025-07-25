package com.wimoor.amazon.inboundV2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.wimoor.amazon.inboundV2.pojo.dto.BoxAnalysisDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.BoxInfo;
import com.wimoor.amazon.inboundV2.pojo.dto.SKUItemDTO;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.service.IShipInboundBoxAnalysisService;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service("shipInboundBoxAnalysis2Service")
public class ShipInboundBoxAnalysis2ServiceImpl implements IShipInboundBoxAnalysisService {

    final IShipInboundItemService iShipInboundItemService;


    @Override
    public List<BoxInfo>  boxAnalysis(UserInfo user, BoxAnalysisDTO dto) {
        int maxbox=0;
        BoxInfo firstBox = dto.getBoxlist().get(0);
        BigDecimal volume = firstBox.getWidth().multiply(firstBox.getLength()).multiply(firstBox.getHeight());
        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalCbm = BigDecimal.ZERO;
        Integer totalQuantity = 0;
        BigDecimal goodVolume = BigDecimal.ZERO;//单个理想体积
        BigDecimal goodWeight = BigDecimal.ZERO;//单个理想重量
        BigDecimal goodCbm = BigDecimal.ZERO;//单个理想材积
        BigDecimal goodBoxNum = BigDecimal.ZERO;//箱子个数预估
        BigDecimal goodBoxWeight = BigDecimal.ZERO;//箱子重量预估
        BigDecimal goodBoxCbmWeight = BigDecimal.ZERO;//箱子材积重预估
        for (SKUItemDTO item : dto.getItemlist()) {
            if (item.getWidth() == null || item.getLength() == null || item.getHeight() == null) {
                throw new BizException("SKU: " + item.getSku() + " 尺寸信息为空");
            }
            BigDecimal itemvolume = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
            totalVolume = totalVolume.add(itemvolume.multiply(new BigDecimal(item.getQuantity())));
            totalWeight = totalWeight.add(item.getWeight().multiply(new BigDecimal(item.getQuantity())));
            totalCbm = totalCbm.add(itemvolume.divide(new BigDecimal(5000), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(item.getQuantity())));
            totalQuantity += item.getQuantity();
            item.setBoxnumber(0);
            item.setBoxquantity(0);
        }
        goodVolume = totalVolume.divide(new BigDecimal(totalQuantity), 2, BigDecimal.ROUND_HALF_UP);
        goodWeight = totalWeight.divide(new BigDecimal(totalQuantity), 2, BigDecimal.ROUND_HALF_UP);
        goodCbm = totalCbm.divide(new BigDecimal(totalQuantity), 2, BigDecimal.ROUND_HALF_UP);
        goodBoxNum = totalVolume.divide(volume, 2, BigDecimal.ROUND_HALF_UP);
        goodBoxCbmWeight = totalCbm.divide(goodBoxNum, 2, BigDecimal.ROUND_HALF_UP);
        goodBoxWeight = totalWeight.divide(goodBoxNum, 2, BigDecimal.ROUND_HALF_UP);
        //加入所有的产品都均匀的放在每一个箱子中goodBoxNum中每个产品的占比等于
        for (SKUItemDTO item : dto.getItemlist()) {
            BigDecimal itemvolume = item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
            BigDecimal leastBoxVolume = itemvolume.multiply(new BigDecimal(item.getQuantity())).divide(totalVolume, 2, BigDecimal.ROUND_HALF_UP).multiply(volume);
            BigDecimal leastBoxWeight = item.getWeight().multiply(new BigDecimal(item.getQuantity())).divide(totalWeight, 2, BigDecimal.ROUND_HALF_UP).multiply(goodBoxWeight);
            splitBoxItem(item, volume, goodBoxWeight, goodBoxNum, leastBoxVolume, leastBoxWeight);
        }
        return groupBox(dto);
    }

      public  List<BoxInfo> groupBox(BoxAnalysisDTO dto){
              BoxInfo firstBox = dto.getBoxlist().get(0);
              BigDecimal volume = firstBox.getWidth().multiply(firstBox.getLength()).multiply(firstBox.getHeight());
              List<BoxInfo> result=null;
                Collections.sort(dto.getItemlist(), new Comparator<SKUItemDTO>() {
                    @Override
                    public int compare(SKUItemDTO o1, SKUItemDTO o2) { return o1.getWeight().compareTo(o2.getWeight());  }
                });

            List<BoxInfo> list=new ArrayList<BoxInfo>();
            while(hasUsableItem(dto.getItemlist())){
                BoxInfo box=new BoxInfo();
                BeanUtil.copyProperties(firstBox,box);
                box.setSkuTotalWeight(new BigDecimal("0"));
                box.setSkuTotalVolumn(new BigDecimal("0"));
                BigDecimal onevolume = volume;
                Integer boxnum=null;
                while(onevolume.compareTo(new BigDecimal("-800"))>0){
                    SKUItemDTO lightitem=null;
                    for(int i=0;i<dto.getItemlist().size();i++){
                        SKUItemDTO item = dto.getItemlist().get(i);
                        if(item.isIsused()==false){
                            if(boxnum!=null&&boxnum.compareTo(item.getBoxnumber())!=0){
                                continue;
                            }
                            lightitem=item;
                            break;
                        }
                    }
                    if(lightitem==null){break;}
                    BigDecimal itemlvolume=lightitem.getWidth().multiply(lightitem.getLength()).multiply(lightitem.getHeight());
                    onevolume=onevolume.subtract(itemlvolume.multiply(new BigDecimal(lightitem.getBoxquantity())));
                    if(onevolume.compareTo(new BigDecimal("-800"))>0){
                        if(box.getItemlist()!=null){
                            lightitem.setIsused(true);
                            box.getItemlist().add(lightitem);
                        }else{
                            box.setItemlist(new ArrayList<SKUItemDTO>());
                            lightitem.setIsused(true);
                            boxnum=lightitem.getBoxnumber();
                            box.getItemlist().add(lightitem);
                        }
                        box.setSkuTotalWeight(box.getSkuTotalWeight().add(lightitem.getWeight().multiply(new BigDecimal(lightitem.getBoxquantity()))));
                        box.setSkuTotalVolumn(box.getSkuTotalVolumn().add(itemlvolume.multiply(new BigDecimal(lightitem.getBoxquantity()))));
                    }else{
                        break;
                    }
                    SKUItemDTO weightitem=null;
                    for(int i=dto.getItemlist().size()-1;i>0;i--){
                        SKUItemDTO item = dto.getItemlist().get(i);
                        if(item.isIsused()==false){
                            if(boxnum!=null&&boxnum.compareTo(item.getBoxnumber())!=0){
                                continue;
                            }
                            weightitem=item;
                            break;
                        }
                    }
                    if(weightitem==null){break;}
                    BigDecimal itemwvolume=weightitem.getWidth().multiply(weightitem.getLength()).multiply(weightitem.getHeight());
                    onevolume=onevolume.subtract(itemwvolume.multiply(new BigDecimal(weightitem.getBoxquantity())));
                    if(onevolume.compareTo(new BigDecimal("-800"))>0){
                        if(box.getItemlist()!=null){
                            weightitem.setIsused(true);
                            box.getItemlist().add(weightitem);
                        }else{
                            box.setItemlist(new ArrayList<SKUItemDTO>());
                            weightitem.setIsused(true);
                            boxnum=weightitem.getBoxnumber();
                            box.getItemlist().add(weightitem);
                        }
                        box.setSkuTotalWeight(box.getSkuTotalWeight().add(weightitem.getWeight().multiply(new BigDecimal(weightitem.getBoxquantity()))));
                        box.setSkuTotalVolumn(box.getSkuTotalVolumn().add(itemwvolume.multiply(new BigDecimal(weightitem.getBoxquantity()))));
                    }else{
                        break;
                    }
                }
                list.add(box);
            }
            int hasgoogbox=0;
            for(BoxInfo box:list){
                box.setIsok(false);
                for(BoxInfo item:dto.getBoxlist()){
                    volume=item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
                    if(box.getSkuTotalVolumn().intValue()-volume.intValue()<2000&&box.getSkuTotalVolumn().intValue()-volume.intValue()>-2000){
                        box.setHeight(item.getHeight());
                        box.setLength(item.getLength());
                        box.setWidth(item.getWidth());
                        box.setIsok(true);
                        hasgoogbox++;
                        break;
                    }
                }
            }
          return list;
    }
    private boolean hasUsableItem(List<SKUItemDTO> itemlist) {
       for(SKUItemDTO item:itemlist){
           if(item.isIsused()==false){
               return true;
           }
       }
       return false;
    }

    private void splitBoxItem(SKUItemDTO item, BigDecimal volume,BigDecimal goodBoxWeight, BigDecimal goodBoxNum,BigDecimal leastBoxVolume ,BigDecimal leastBoxWeight) {
        BigDecimal itemvolume=item.getWidth().multiply(item.getLength()).multiply(item.getHeight());
        int[] boxnums={5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100};
        for(int i=0;i<=boxnums.length-1&&boxnums[i]<goodBoxNum.intValue()+5;i++){
            int singlenumber = item.getQuantity() / boxnums[i];
            BigDecimal boxinvolume = itemvolume.multiply(new BigDecimal(singlenumber));
            BigDecimal boxinWeight = item.getWeight().multiply(new BigDecimal(singlenumber));
            BigDecimal cbm=item.getWidth().multiply(item.getLength()).multiply(item.getHeight()).divide(new BigDecimal(5000), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal weight=item.getWeight();
                if(leastBoxVolume.compareTo(boxinvolume)>=0||leastBoxWeight.compareTo(boxinWeight)>=0){
                        item.setBoxnumber(boxnums[i]);
                        item.setBoxquantity(item.getQuantity() / boxnums[i]);
                        item.setIsused(false);
                        return;
                }
            if((boxinvolume.compareTo(volume)>=0
                    ||boxinWeight.compareTo(goodBoxWeight)>=0)
                    ||boxinWeight.compareTo(new BigDecimal(22))>=0)  {
                continue;
            }else{
                item.setBoxnumber(boxnums[i]);
                item.setBoxquantity(singlenumber);
                item.setIsused(false);
                return;
            }
        }

    }
}
