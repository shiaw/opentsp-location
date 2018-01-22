package com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.acceptor.da.DACommand;
import com.navinfo.opentsp.platform.dp.core.cache.DictCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.DictEntity;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCGetDictDataRes;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component(value = "da_1910_GetDictDataRes")
public class DA_0910_GetDictDataRes extends DACommand {

    @Resource
    private DictCache dictCache;

    @Override
    public int processor(Packet packet) {
        try {
            long s = System.currentTimeMillis();
            LCGetDictDataRes.GetDictDataRes dictDataRes = LCGetDictDataRes.GetDictDataRes.parseFrom(packet.getContent());
            List<LCDictData.DictData> datas = dictDataRes.getDatasList();
            for (LCDictData.DictData dictData : datas) {
                DictEntity dictEntity = new DictEntity();
                dictEntity.setDataCode(dictData.getDataCode());
                dictEntity.setDictType(dictData.getDictType());
                dictEntity.setName(dictData.getName());
                if (dictData.hasGbCode()) {
                    dictEntity.setGbCode(dictData.getGbCode());
                }
                if (dictData.hasParentDataCode()) {
                    dictEntity.setParentDataCode(dictData.getParentDataCode());
                }
                if (dictData.hasDictValue()) {
                    dictEntity.setDictValue(dictData.getDictValue());
                }
                dictCache.addDict(dictEntity);
            }
            logger.error("加载DA-RMI(0910)成功!,耗时:{}",System.currentTimeMillis()-s);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
