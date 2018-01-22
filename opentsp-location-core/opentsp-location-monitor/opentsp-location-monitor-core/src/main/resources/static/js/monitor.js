var refreshInterval = 30000;//设置刷新时间
var segNum = 10;//x轴分段数
function parseDate(date) {
    var datetime =
        (date.getHours() < 10 ? "0" + date.getHours() : date
            .getHours())
        + ":"
        + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
            .getMinutes())
        + ":"
        + (date.getSeconds() < 10 ? "0" + date.getSeconds() : date
            .getSeconds());
    return datetime;
}
function createAllChart(init){
    var taNodes = [];
    var dpNodes = [];
    var taSeries = new Array();
    var outTaSeries = new Array();
    var dpSeries = new Array();
    var rpSeries = new Array();
    var daSeries = new Array();
    //定义节点数据
    var nodeData = new Array();
    nodeData.push({"name": "T","x": 50,"y": 200, itemStyle:{normal:{color: '#99CC32'}}});//T节点
    nodeData.push({"name": "KAFKA","x": 500,"y": 200, itemStyle:{normal:{color: '#FF00FF'}}});//KAFKA节点
    nodeData.push({"name": "RP","x": 1200,"y": 120, itemStyle:{normal:{color: '#00FF00'}}});//RP节点
    nodeData.push({"name": "DA","x": 1200,"y": 470, itemStyle:{normal:{color: '#00FF00'}}});//DA节点
    //定义连线数据
    var linksData = new Array();
    $.ajax({
        url: "http://192.168.135.156:8090/location/monitor/locationMonitor",
        type: "get",
        headers: {
            Accept: "application/json"
        },
        success : function(data) {
            var ta_map = data.data.taLocationMonitorData;
            var dp_map = data.data.dpLocationMonitorData;
            var num = 60;
            var i=0;
            $.each(ta_map,function(k,v){
                taNodes.push(k);
                //动态ta节点
                nodeData.push({"name": k,"x": 250,"y": num+200*i, itemStyle:{normal:{color: '#00FFFF'}}});
                $.each(v,function(key,value){
                    var text = '';
                    var stack = '';
                    if(value.direct && value.name=='KAFKA'){
                        text=k+'→'+value.name;
                        stack='TA→'+value.name;
                        outTaSeries.push(
                            {   name:k,
                                type:'line',
                                data:(function (){
                                    if(init == 0){
                                        var res = [];
                                        var len = segNum;
                                        while (len--) {
                                            if(len == 0){
                                                res.push(value.value);
                                            }else{
                                                res.push('');
                                            }
                                        }
                                    }else{
                                        for (var l = 0; l < outTaSeries_bak.length; l++) {
                                            if(outTaSeries_bak[l].name==k){
                                                var res = outTaSeries_bak[l].data;
                                                if(res != undefined){
                                                    res.shift();
                                                    res.push(value.value);
                                                }else{
                                                    var len = segNum;
                                                    while (len--) {
                                                        if(len == 0){
                                                            res.push(value.value);
                                                        }else{
                                                            res.push('');
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    return res;
                                })()
                            });
                    }else if(value.direct==false){
                        text = value.name + '→' + k;
                        stack = value.name + '→TA';
                        taSeries.push(
                            {   name:k,
                                type:'line',
                                data:(function (){
                                    if(init == 0){
                                        var res = [];
                                        var len = segNum;
                                        while (len--) {
                                            if(len == 0){
                                                res.push(value.value);
                                            }else{
                                                res.push('');
                                            }
                                        }
                                    }else{
                                        for (var l = 0; l < taSeries_bak.length; l++) {
                                            if(taSeries_bak[l].name==k){
                                                var res = taSeries_bak[l].data;
                                                if(res != undefined){
                                                    res.shift();
                                                    res.push(value.value);
                                                }else{
                                                    var len = segNum;
                                                    while (len--) {
                                                        if(len == 0){
                                                            res.push(value.value);
                                                        }else{
                                                            res.push('');
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    return res;
                                })()
                            });
                    }
                    //画线
                    if(value.direct){
                        linksData.push({source: k,
                            target: value.name,
                            count: value.value,
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function(x){return x.data.count;}
                                }
                            },
                            lineStyle: {
                                normal: {
                                    color:'#0000FF',
                                    curveness: 0.2
                                }
                            }
                        });
                    }else{
                        linksData.push({source: value.name,
                            target: k,
                            count: value.value,
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function(x){return x.data.count;}
                                }
                            },
                            lineStyle: {
                                normal: { color:'#FF1111',curveness: 0.2 }
                            }
                        });
                    }
                });
                i++;
            });
            var j=0;
            $.each(dp_map,function(k,v){
                //动态dp节点
                dpNodes.push(k);
                nodeData.push({"name": k,"x": 650,"y": num+100*j, itemStyle:{normal:{color: '#FFFF00'}}});
                $.each(v,function(key,value){
                    var text = '';
                    var stack = '';
                    if(value.direct){
                        text=k+'→'+value.name;
                        stack='DP→'+value.name;
                        if(value.name=='RP'){
                            rpSeries.push(
                                {   name:k,
                                    type:'line',
                                    data:(function (){
                                        if(init == 0){
                                            var res = [];
                                            var len = segNum;
                                            while (len--) {
                                                if(len == 0){
                                                    res.push(value.value);
                                                }else{
                                                    res.push('');
                                                }
                                            }
                                        }else{
                                            for (var l = 0; l < rpSeries_bak.length; l++) {
                                                if(rpSeries_bak[l].name==k){
                                                    var res = rpSeries_bak[l].data;
                                                    if(res != undefined){
                                                        res.shift();
                                                        res.push(value.value);
                                                    }else{
                                                        var len = segNum;
                                                        while (len--) {
                                                            if(len == 0){
                                                                res.push(value.value);
                                                            }else{
                                                                res.push('');
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        return res;})()
                                });
                        }else if(value.name=='DA'){
                            daSeries.push(
                                {   name:k,
                                    type:'line',
                                    data:(function (){
                                        if(init == 0){
                                            var res = [];
                                            var len = segNum;
                                            while (len--) {
                                                if(len == 0){
                                                    res.push(value.value);
                                                }else{
                                                    res.push('');
                                                }
                                            }
                                        }else{
                                            for (var l = 0; l < daSeries_bak.length; l++) {
                                                if(daSeries_bak[l].name==k){
                                                    var res = daSeries_bak[l].data;
                                                    if(res != undefined){
                                                        res.shift();
                                                        res.push(value.value);
                                                    }else{
                                                        var len = segNum;
                                                        while (len--) {
                                                            if(len == 0){
                                                                res.push(value.value);
                                                            }else{
                                                                res.push('');
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        return res;})()
                                });
                        }
                    }else {
                        text = value.name + '→' + k;
                        stack = value.name + '→DP';
                        dpSeries.push(
                            {   name:k,
                                type:'line',
                                data:(function (){
                                    if(init == 0){
                                        var res = [];
                                        var len = segNum;
                                        while (len--) {
                                            if(len == 0){
                                                res.push(value.value);
                                            }else{
                                                res.push('');
                                            }
                                        }
                                    }else{
                                        for (var l = 0; l < dpSeries_bak.length; l++) {
                                            if(dpSeries_bak[l].name==k){
                                                var res = dpSeries_bak[l].data;
                                                if(res != undefined){
                                                    res.shift();
                                                    res.push(value.value);
                                                }else{
                                                    var len = segNum;
                                                    while (len--) {
                                                        if(len == 0){
                                                            res.push(value.value);
                                                        }else{
                                                            res.push('');
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    return res;})()
                            });
                    }
                    //画线
                    if(value.name=='RP'){
                        linksData.push({source: k,
                            target: value.name,
                            count: value.value,
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function(x){return x.data.count;}
                                }
                            },
                            lineStyle: {
                                normal: { color:'#FF1111',curveness: 0.2 }
                            }
                        });
                    }else if(value.direct){
                        linksData.push({source: k,
                            target: value.name,
                            count: value.value,
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function(x){return x.data.count;}
                                }
                            },
                            lineStyle: {
                                normal: { color:'#0000FF',curveness: 0.2 }
                            }
                        });
                    }else{
                        linksData.push({source: value.name,
                            target: k,
                            count: value.value,
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function(x){return x.data.count;}
                                }
                            },
                            lineStyle: {
                                normal: { color:'#FF1111',curveness: 0.2 }
                            }
                        });
                    }
                });
                j++;
            });

            createTaChart(taNodes,taSeries);
            createOutTaChart(taNodes,outTaSeries);
            createDpChart(dpNodes,dpSeries);
            createRpChart(dpNodes,rpSeries);
            createDaChart(dpNodes,daSeries);

            // 基于准备好的dom，初始化echarts实例
            var allChart = echarts.init(document.getElementById('allChart'));
            // 指定图表的配置项和数据
            var allOption = {
                title: {
                    text: 'TA、DP模块进出数据量统计',
                    subtext:parseDate(new Date())
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                animationDurationUpdate: 1500,
                animationEasingUpdate: 'quinticInOut',
                series : [
                    {
                        type: 'graph',
                        layout: 'none',
                        symbolSize: 50,
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    color:"#000000"
                                }
                            }
                        },
                        edgeSymbol: ['circle', 'arrow'],
                        edgeSymbolSize: [4, 10],
                        edgeLabel: {
                            normal: {
                                textStyle: {
                                    fontSize: 20
                                }
                            }
                        },
                        data: nodeData,
                        links: linksData
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            allChart.setOption(allOption);
        }
    });
}
function createTaChart(taNodes,taSeries){
    var taOption = {
        title : {
            text: '进TA动态数据'
        },
        tooltip : {
            trigger: 'axis',
            formatter: function (params,ticket) {
                var count = 0;
                var res =  params[0].name;
                for (var i = 0, l = params.length; i < l; i++) {
                    res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
                    count += params[i].value;
                }
                res += '<br/>总量: ' + count;
                return res;
            }
        },
        legend: {
            data:taNodes,
            x:'center',
            y:'top'
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : true,
                splitNumber : segNum,
                data : (function (){
                    var now = new Date();
                    var res = [];
                    var len = segNum;
                    while (len--) {
                        res.unshift(parseDate(now));
                        now = new Date(now - refreshInterval);
                    }
                    return res;
                })()
            }
        ],
        yAxis : [
            {
                type : 'value',
                min : 0,
                minInterval : 1,
                scale: true,
                name : '数量',
                splitLine:{show: false}
            }
        ],
        series : taSeries
    };
    taChart.setOption(taOption,true);
    taSeries_bak = taSeries;
}
function createOutTaChart(taNodes,outTaSeries){
    var outTaOption = {
        title : {
            text: '出TA动态数据'
        },
        tooltip : {
            trigger: 'axis',
            formatter: function (params,ticket) {
                var count = 0;
                var res =  params[0].name;
                for (var i = 0, l = params.length; i < l; i++) {
                    res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
                    count += params[i].value;
                }
                res += '<br/>总量: ' + count;
                return res;
            }
        },
        legend: {
            data:taNodes,
            x:'center',
            y:'top'
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : true,
                splitNumber : segNum,
                data : (function (){
                    var now = new Date();
                    var res = [];
                    var len = segNum;
                    while (len--) {
                        res.unshift(parseDate(now));
                        now = new Date(now - refreshInterval);
                    }
                    return res;
                })()
            }
        ],
        yAxis : [
            {
                type : 'value',
                min : 0,
                minInterval : 1,
                scale: true,
                name : '数量',
                splitLine:{show: false}
            }
        ],
        series : outTaSeries
    };
    outTaChart.setOption(outTaOption,true);
    outTaSeries_bak = outTaSeries;
}
function createDpChart(dpNodes,dpSeries){
    var dpOption = {
        title : {
            text: '进DP动态数据'
        },
        tooltip : {
            trigger: 'axis',
            formatter: function (params,ticket) {
                var count = 0;
                var res =  params[0].name;
                for (var i = 0, l = params.length; i < l; i++) {
                    res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
                    count += params[i].value;
                }
                res += '<br/>总量: ' + count;
                return res;
            }
        },
        legend: {
            data:dpNodes,
            x:'center',
            y:'top'
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : true,
                splitNumber : segNum,
                data : (function (){
                    var now = new Date();
                    var res = [];
                    var len = segNum;
                    while (len--) {
                        res.unshift(parseDate(now));
                        now = new Date(now - refreshInterval);
                    }
                    return res;
                })()
            }
        ],
        yAxis : [
            {
                type : 'value',
                min : 0,
                minInterval : 1,
                scale: true,
                name : '数量',
                splitLine:{show: false}
            }
        ],
        series : dpSeries
    };
    dpChart.setOption(dpOption,true);
    dpSeries_bak = dpSeries;
}
function createRpChart(dpNodes,rpSeries){
    var rpOption = {
        title : {
            text: '进RP动态数据'
        },
        tooltip : {
            trigger: 'axis',
            formatter: function (params,ticket) {
                var count = 0;
                var res =  params[0].name;
                for (var i = 0, l = params.length; i < l; i++) {
                    res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
                    count += params[i].value;
                }
                res += '<br/>总量: ' + count;
                return res;
            }
        },
        legend: {
            data:dpNodes,
            x:'center',
            y:'top'
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : true,
                splitNumber : segNum,
                data : (function (){
                    var now = new Date();
                    var res = [];
                    var len = segNum;
                    while (len--) {
                        res.unshift(parseDate(now));
                        now = new Date(now - refreshInterval);
                    }
                    return res;
                })()
            }
        ],
        yAxis : [
            {
                type : 'value',
                min : 0,
                minInterval : 1,
                scale: true,
                name : '数量',
                splitLine:{show: false}
            }
        ],
        series : rpSeries
    };
    rpChart.setOption(rpOption,true);
    rpSeries_bak = rpSeries;
}
function createDaChart(dpNodes,daSeries){
    var daOption = {
        title : {
            text: '进DA动态数据'
        },
        tooltip : {
            trigger: 'axis',
            formatter: function (params) {
                var count = 0;
                var res =  params[0].name;
                for (var i = 0, l = params.length; i < l; i++) {
                    res += '<br/>' + params[i].seriesName + ' : ' + params[i].value;
                    count += params[i].value;
                }
                res += '<br/>总量: ' + count;
                return res;
            }
        },
        legend: {
            data:dpNodes,
            x:'center',
            y:'top'
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : true,
                splitNumber : segNum,
                data : (function (){
                    var now = new Date();
                    var res = [];
                    var len = segNum;
                    while (len--) {
                        res.unshift(parseDate(now));
                        now = new Date(now - refreshInterval);
                    }
                    return res;
                })()
            }
        ],
        yAxis : [
            {
                type : 'value',
                min : 0,
                minInterval : 1,
                scale: true,
                name : '数量',
                splitLine:{show: false}
            }
        ],
        series : daSeries
    };
    daChart.setOption(daOption,true);
    daSeries_bak = daSeries;
}
var taSeries_bak = new Array();
var outTaSeries_bak = new Array();
var dpSeries_bak = new Array();
var rpSeries_bak = new Array();
var daSeries_bak = new Array();
var taChart = echarts.init(document.getElementById('taChart'));
var outTaChart = echarts.init(document.getElementById('outTaChart'));
var dpChart = echarts.init(document.getElementById('dpChart'));
var rpChart = echarts.init(document.getElementById('rpChart'));
var daChart = echarts.init(document.getElementById('daChart'));
createAllChart(0);
setInterval(function()
{
    createAllChart(1);
},refreshInterval); //定时刷新图表