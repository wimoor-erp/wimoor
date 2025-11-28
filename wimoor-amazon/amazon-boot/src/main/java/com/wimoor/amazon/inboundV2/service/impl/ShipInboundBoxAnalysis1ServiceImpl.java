package com.wimoor.amazon.inboundV2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.wimoor.amazon.inboundV2.pojo.dto.BoxAnalysisDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.BoxInfo;
import com.wimoor.amazon.inboundV2.pojo.dto.SKUItemDTO;
import com.wimoor.amazon.inboundV2.service.IShipInboundBoxAnalysisService;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service("shipInboundBoxAnalysis1Service")
@RequiredArgsConstructor
public class ShipInboundBoxAnalysis1ServiceImpl implements IShipInboundBoxAnalysisService {


    @Override
    public List<BoxInfo> boxAnalysis(UserInfo user, BoxAnalysisDTO dto) {
        handleBoxItemSort(dto.getItemlist());
        return  handlePackage(dto);
    }



    public void handleBoxItemSort(List<SKUItemDTO> itemlist){
        //用每个sku的单个重量/数量
        Collections.sort(itemlist, new Comparator<SKUItemDTO>() {
            @Override
            public int compare(SKUItemDTO o1, SKUItemDTO o2) {
                double sortkey1 = o1.getWeight().doubleValue();
                double sortkey2=o2.getWeight().doubleValue();
                if(sortkey1>sortkey2){
                    return 1;
                }else if(sortkey1<sortkey2){
                    return -1;
                }else {
                    return 0;
                }
            }
        });
    }

    public List<BoxInfo> handlePackage(BoxAnalysisDTO dto){
        List<BoxInfo> resultItem=new ArrayList<BoxInfo>();
        while(hasUsableItem(dto.getItemlist())){
           Integer oldresultsize=resultItem.size();
            for(BoxInfo box:dto.getBoxlist()){
                BoxInfo mybox=new BoxInfo();
                BeanUtil.copyProperties(box,mybox);
                List<SKUItemDTO> inBoxItemlist=new ArrayList<SKUItemDTO>();
                BigDecimal volume=mybox.getWidth().multiply(mybox.getLength()).multiply(mybox.getHeight());
                while(handleGroupItem(dto.getItemlist(),inBoxItemlist,volume,true)){
                    if(inBoxItemlist.size()>0&&checkItemIsUsed(mybox,inBoxItemlist)){
                        mybox.setIsok(true);
                        resultItem.add(mybox);
                        inBoxItemlist=new ArrayList<SKUItemDTO>();
                        mybox=new BoxInfo();
                        BeanUtil.copyProperties(box,mybox);
                    }
                }
            }
            if(oldresultsize==resultItem.size()){
                break;
            }else{
                oldresultsize=resultItem.size();
            }
        }
        Set<String> hashSet=new HashSet<String>();
        for(BoxInfo box:resultItem){
            for(SKUItemDTO item:box.getItemlist()) {
                hashSet.add(item.getSku());
            }
         }
        BoxInfo mybox=new BoxInfo();
        for(SKUItemDTO item:dto.getItemlist()) {
             if(!hashSet.contains(item.getSku())){
                 if(mybox.getItemlist()==null){
                     mybox.setItemlist(new ArrayList<SKUItemDTO>());
                 }
                 mybox.getItemlist().add(item);
             }
        }
        if(mybox.getItemlist()!=null&&mybox.getItemlist().size()>0){
            resultItem.add(mybox);
        }
        return resultItem;
    }

    private boolean hasUsableItem(List<SKUItemDTO> itemlist) {
        for(SKUItemDTO item:itemlist){
            if(item.isIsused()==false){
                return true;
            }
        }
        return false;
    }

    private boolean checkItemIsUsed(BoxInfo box, List<SKUItemDTO> inBoxItemlist) {
        BigDecimal totalVolumn = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;
        List<SKUItemDTO> list=new ArrayList<SKUItemDTO>();
        for(SKUItemDTO item:inBoxItemlist){
            if(item.isIsused()==false){
                return false;
            }
            totalVolumn=totalVolumn.add(item.getLength().multiply(item.getWidth()).multiply(item.getHeight()).multiply(new BigDecimal(item.getBoxquantity())));
            totalWeight=totalWeight.add(item.getWeight().multiply(new BigDecimal(item.getBoxquantity())));
        }
        box.setItemlist(inBoxItemlist);
        box.setSkuTotalWeight(totalWeight);
        box.setSkuTotalVolumn(totalVolumn);
        return true;
    }

    //每个箱子放的数量
    boolean checkVolume(List<SKUItemDTO> items, BigDecimal volume){
        BigDecimal totalVolumn=BigDecimal.ZERO;
        for(SKUItemDTO item:items){
            totalVolumn=totalVolumn.add(item.getWidth().multiply(item.getLength()).multiply(item.getHeight()).multiply(new BigDecimal(item.getQuantity())));
        }
        List<SKUItemDTO> itemlist=new ArrayList<SKUItemDTO>();
        Integer boxnumber=totalVolumn.divide(volume, 0, RoundingMode.HALF_DOWN).intValue();
        if(boxnumber>=5){
            for(SKUItemDTO item:items){
                if(item.getQuantity()%boxnumber!=0){
                    break;
                }else{
                    item.setBoxquantity(item.getQuantity()/boxnumber);
                    item.setBoxnumber(boxnumber);
                    itemlist.add(item);
                }
            }
        }

       return itemlist.size()== items.size();
    }

    SKUItemDTO getLight(List<SKUItemDTO> itemlist,List<SKUItemDTO> grouplist){
        for(int j=0;j<itemlist.size();j++){
            SKUItemDTO item=itemlist.get(j);
            if(item.isIsused()==false&&!grouplist.contains(item)){
                return item;
            }
        }
        return null;
    }
    SKUItemDTO getWeight(List<SKUItemDTO> itemlist,List<SKUItemDTO> grouplist){
        for(int j=itemlist.size()-1;j>=0;j--){
            SKUItemDTO item=itemlist.get(j);
            if(item.isIsused()==false&&!grouplist.contains(item)){
                return item;
            }
        }
        return null;
    }

    boolean checkCmbGTWeigth(List<SKUItemDTO> items,SKUItemDTO one){
        BigDecimal boxcbm = new BigDecimal("0");
        BigDecimal boxweight = new BigDecimal("0");
        for(SKUItemDTO item:items) {
            BigDecimal singlevolume=item.getLength().multiply(item.getWidth()).multiply(item.getHeight());
            boxcbm =boxcbm.add( singlevolume.divide(new BigDecimal(5000), 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(item.getQuantity())));
            boxweight=boxweight.add( item.getWeight().multiply(new BigDecimal(item.getQuantity())));
        }
        BigDecimal singlevolume=one.getLength().multiply(one.getWidth()).multiply(one.getHeight());
        boxcbm =boxcbm.add( singlevolume.divide(new BigDecimal(5000), 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(one.getQuantity())));
        boxweight=boxweight.add( one.getWeight().multiply(new BigDecimal(one.getQuantity())));
        return boxcbm.compareTo(boxweight)>0;
    }

   boolean handleGroupItem(List<SKUItemDTO> itemlist,List<SKUItemDTO> grouplist,BigDecimal volume,boolean needlight){
        if(grouplist.size()<itemlist.size()){
            if(needlight){
                SKUItemDTO one = getLight(itemlist,grouplist);
                if(one!=null){
                    if(checkCmbGTWeigth(grouplist,one)){
                        if(grouplist.size()==0){
                            grouplist.add(one);
                        }
                       return handleGroupItem( itemlist,  grouplist,volume,false);
                    }else{
                        grouplist.add(one);
                        if(checkVolume(grouplist,volume)){
                            //ok
                            for(SKUItemDTO item:grouplist){
                                item.setIsused(true);
                            }
                            return true;
                        }else{
                           return handleGroupItem( itemlist,  grouplist,volume,true);
                        }
                    }
                }else {
                    return false;
                }
            }else {
                SKUItemDTO one = getWeight(itemlist,grouplist);
                if (one != null) {
                    if (checkCmbGTWeigth(grouplist,one)) {
                        grouplist.add(one);
                       return handleGroupItem(itemlist, grouplist, volume, false);
                    } else {
                        grouplist.add(one);
                        if (checkVolume(grouplist, volume)) {
                            //ok
                            for (SKUItemDTO item : grouplist) {
                                item.setIsused(true);
                            }
                            return true;
                        } else {
                          return  handleGroupItem(itemlist, grouplist, volume, true);
                        }
                    }
                }else{
                    return false;
                }
            }
        }else{
            return false;
        }
    }
    void subUseableItemQuantity(List<SKUItemDTO> itemlist,BoxInfo box){
        Map<String,Integer> skuBoxQuantityMap=new HashMap<String,Integer>();
        for(SKUItemDTO item:box.getItemlist()) {
            skuBoxQuantityMap.put(item.getSku(),item.getBoxquantity());
        }
        for(SKUItemDTO item:itemlist) {
            if (skuBoxQuantityMap.containsKey(item.getSku())) {
                item.setQuantity(item.getQuantity() - skuBoxQuantityMap.get(item.getSku()));
            }
        }

    }

    private void handAddBox(SKUItemDTO item,int boxnum, int item1boxquantity, BoxInfo box) {
        SKUItemDTO itemone=new SKUItemDTO();
        BeanUtil.copyProperties(item,itemone);
        itemone.setBoxnumber(boxnum);
        itemone.setBoxquantity(item1boxquantity);
        box.setItemlist(Arrays.asList(itemone));
    }




}
