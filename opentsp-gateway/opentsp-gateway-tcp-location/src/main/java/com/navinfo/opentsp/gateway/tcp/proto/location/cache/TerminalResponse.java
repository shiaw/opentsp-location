package com.navinfo.opentsp.gateway.tcp.proto.location.cache;


import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDownCommonRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDownCommonRes.DownCommonRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAnswerQuestion;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAnswerQuestion.AnswerQuestion;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMediaDataQueryRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMediaDataQueryRes.MediaDataQueryRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControlRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControlRes.TerminalStatusControlRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.*;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectDriverIdentityRes.TRCollectDriverIdentityRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectMileageRes.TRCollectMileageRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectOvertimeRecordRes.TRCollectOvertimeRecordRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectRealTimeRes.TRCollectRealTimeRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectSpeedLogRes.TRCollectSpeedLogRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectVehicleInfoRes.TRCollectVehicleInfoRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRDoubtCollectionRes.TRDoubtCollectionRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRVersionRes.TRVersionRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCParameterQueryRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCParameterQueryTextRes;

import static com.navinfo.opentsp.platform.location.kit.LCConstant.*;
import static com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType.*;

public final class TerminalResponse {
    /**
     * 超时/失败/终端未注册/掉线
     * 模拟终端应答<br>
     * 当内部消息ID是0,则到终端指令超时缓存查找内部消息ID
     *
     * @param commandId
     *            {@link Integer}内部消息ID
     * @param terminalId
     *            {@link Long} 终端ID
     * @param result
     *            {@link ResponseResult} 响应结果
     * @param responseSerialNumber
     *            {@link Integer} 应答流水号
     * @return
     */
    public static Packet response(int commandId, long terminalId,
                                  ResponseResult result, int responseSerialNumber) {
        if(commandId == 0){
            AnswerEntry answerEntry = AnswerCommandCache.getInstance()
                    .getAnswerEntry("0" + terminalId, responseSerialNumber, true);
            if (answerEntry != null) {
                commandId = answerEntry.getInternalCommand();
            }
        }
        switch (commandId) {
            case AllCommands.Terminal.CallName_VALUE:
//			return TerminalResponse.pk3050(terminalId, responseSerialNumber,
//					result);

            case Constant.JTProtocol.QueryTerminalParameters:
                return TerminalResponse.pk3302(terminalId, responseSerialNumber,
                        result);
            case Constant.JTProtocol.QueryAppointTerminalParameters:
                return TerminalResponse.pk3302(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.ParameterQueryTextRes_VALUE:
                return TerminalResponse.pk3303(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.AskQuestion_VALUE:
                return TerminalResponse.pk3160(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.ParameterQuery_VALUE:
                return TerminalResponse.pk3302(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.QueryAppointPara_VALUE:
                return TerminalResponse.pk3302(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRVersionCollect_VALUE:
                return TerminalResponse.pk3055(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRSpeedCollect_VALUE:
                return TerminalResponse.pk3056(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRDoubtCollect_VALUE:
                return TerminalResponse.pk3057(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRPulseCollection_VALUE:
                return TerminalResponse.pk3058(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectDriverInfo_VALUE:
                return TerminalResponse.Collect_3060(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectRealTime_VALUE:
                return TerminalResponse.Collect_3061(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectMileage_VALUE:
                return TerminalResponse.Collect_3062(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectVehicleInfo_VALUE:
                return TerminalResponse.Collect_3063(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectStatusSignal_VALUE:
                return TerminalResponse.Collect_3064(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectOnlyCode_VALUE:
                return TerminalResponse.Collect_3065(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectLocationData_VALUE:
                return TerminalResponse.Collect_3066(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectOvertimeRecord_VALUE:
                return TerminalResponse.Collect_3067(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectDriverIdentity_VALUE:
                return TerminalResponse.Collect_3068(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectPowerRecord_VALUE:
                return TerminalResponse.Collect_3069(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TRCollectParaModifyRecord_VALUE:
                return TerminalResponse.Collect_3070(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.MediaDataQuery_VALUE:
                return TerminalResponse.Collect_3165(terminalId, responseSerialNumber,
                        result);
            case AllCommands.Terminal.TerminalStatusControl_VALUE:
                return TerminalResponse.Collect_3170(terminalId, responseSerialNumber,
                        result);
            default:
                return TerminalResponse.pkDefalut(terminalId, responseSerialNumber,
                        result, commandId);
        }
    }

    private static Packet Collect_3165(long terminalId,
                                       int responseSerialNumber, ResponseResult result) {
        MediaDataQueryRes.Builder builder = MediaDataQueryRes.newBuilder();

        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.MediaDataQueryRes_VALUE);
        pk.setProtocol(TERMINAL);
        pk.setUniqueMark("0" + terminalId);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    private static Packet pk3160(long terminalId, int responseSerialNumber,
                                 ResponseResult result) {
        AnswerQuestion.Builder builder = AnswerQuestion.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        //builder.setResult(result);

        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.AnswerQuestion_VALUE);
        pk.setProtocol(TERMINAL);
        pk.setUniqueMark("0" + terminalId);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    private static Packet pkDefalut(long terminalId, int responseSerialNumber,
                                    ResponseResult result, int internalCommand) {
        // AnswerEntry answerEntry =
        // AnswerCommandCache.getInstance().getAnswerEntry("0"+terminalId,
        // responseSerialNumber, true);
        DownCommonRes.Builder builder = DownCommonRes.newBuilder();
        builder.setResponseId(internalCommand);
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);

        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.DownCommonRes_VALUE);
        pk.setProtocol(TERMINAL);
        pk.setUniqueMark("0" + terminalId);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    private static Packet pk3303(long terminalId, int responseSerialNumber,
                                 ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.ParameterQueryTextRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);
        LCParameterQueryTextRes.ParameterQueryTextRes.Builder builder = LCParameterQueryTextRes.ParameterQueryTextRes
                .newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    private static Packet pk3302(long terminalId, int responseSerialNumber,
                                 ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.ParameterQueryRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);
        LCParameterQueryRes.ParameterQueryRes.Builder builder = LCParameterQueryRes.ParameterQueryRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    /**/
    private static Packet pk3055(long terminalId, int responseSerialNumber,
                                 ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRVersionRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);
        TRVersionRes.Builder builder = TRVersionRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    /**/
    private static Packet pk3056(long terminalId, int responseSerialNumber,
                                 ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRSpeedCollectionRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);
        LCTRSpeedCollectionRes.TRSpeedCollectionRes.Builder builder = LCTRSpeedCollectionRes.TRSpeedCollectionRes
                .newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    private static Packet pk3057(long terminalId, int responseSerialNumber,
                                 ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRDoubtCollectionRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);
        TRDoubtCollectionRes.Builder builder = TRDoubtCollectionRes
                .newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    private static Packet pk3058(long terminalId, int responseSerialNumber,
                                 ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRPulseCollectionRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);
        LCTRPulseCollectionRes.TRPulseCollectionRes.Builder builder = LCTRPulseCollectionRes.TRPulseCollectionRes
                .newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

    private static Packet Collect_3060(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectRealTimeRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        LCTRCollectDriverInfoRes.TRCollectDriverInfoRes.Builder builder = LCTRCollectDriverInfoRes.TRCollectDriverInfoRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3061(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectRealTimeRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        TRCollectRealTimeRes.Builder builder = TRCollectRealTimeRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3062(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectMileageRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        TRCollectMileageRes.Builder builder = TRCollectMileageRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3063(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectVehicleInfoRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(LCConstant.LCMessageType.TERMINAL);

        //
        TRCollectVehicleInfoRes.Builder builder = TRCollectVehicleInfoRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3064(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectStatusSignalRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(LCConstant.LCMessageType.TERMINAL);

        //
        TRCollectStatusSignalRes.Builder builder = TRCollectStatusSignalRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3065(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectOnlyCodeRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(LCConstant.LCMessageType.TERMINAL);

        //
        LCTRCollectOnlyCodeRes.TRCollectOnlyCodeRes.Builder builder = LCTRCollectOnlyCodeRes.TRCollectOnlyCodeRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3066(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectLocationDataRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(LCConstant.LCMessageType.TERMINAL);

        //
        LCTRCollectLocationDataRes.TRCollectLocationDataRes.Builder builder = LCTRCollectLocationDataRes.TRCollectLocationDataRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3067(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectOvertimeRecordRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        TRCollectOvertimeRecordRes.Builder builder = TRCollectOvertimeRecordRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3068(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectDriverIdentityRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        TRCollectDriverIdentityRes.Builder builder = TRCollectDriverIdentityRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3069(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectPowerRecordRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        LCTRCollectPowerRecordRes.TRCollectPowerRecordRes.Builder builder = LCTRCollectPowerRecordRes.TRCollectPowerRecordRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3070(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectParaModifyRecordRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes.Builder builder = LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3071(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TRCollectSpeedLogRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        TRCollectSpeedLogRes.Builder builder = TRCollectSpeedLogRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }
    private static Packet Collect_3170(long terminalId, int responseSerialNumber,
                                       ResponseResult result) {
        Packet pk = new Packet(true);
        pk.setCommand(AllCommands.Terminal.TerminalStatusControlRes_VALUE);
        pk.setUniqueMark("0" + terminalId);
        pk.setFrom(terminalId);
        pk.setProtocol(TERMINAL);

        //
        TerminalStatusControlRes.Builder builder = TerminalStatusControlRes.newBuilder();
        builder.setSerialNumber(responseSerialNumber);
        builder.setResult(result);
        pk.setContent(builder.build().toByteArray());
        return pk;
    }

}
