$(function () {
    refresh(null);
});

function refresh(jsonStr){
    if(jsonStr==null){
        return;
    }
    var json = JSON.parse(jsonStr);
    $(document).ready(function () {
        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });

        Highcharts.chart('container', {
            chart: {
                type: 'spline',
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10,
                events: {
                    load: function () {
                        // set up the updating of the chart each second
                        var series = this.series[0];


                                        if(json["status"]==1){
                                            $("#lineStatus").html("在线");
                                            var x =(new Date()).getTime(), // current time
                                                y =parseFloat(json["temp"]);
                                            series.addPoint([x,y], true, true);
                                            this.tooltip;
                                        } else $("#lineStatus").html("离线");

                    }
                }
            },
            title: {
                text: '温度实时监控'
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 50
            },
            yAxis: {
                title: {
                    text: 'temp'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%H:%M:%S', this.x) + '<br/>' +
                        Highcharts.numberFormat(this.y, 2);
                }
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
                    series: [{
                name: '温度：',
                data: (function () {
                    // generate an array of random data
                    var arr=new Array();
                    var temp;

                    var data = [],
                        time = (new Date()).getTime(),
                        i;

                        $.ajax({

                                temp=JSON.parse(msg);
                                if(json["status"]==0)
                                    $("#lineStatus").html("离线");
                                else $("#lineStatus").html("在线");

                        });
                    for (i = -19; i <=0; i +=1) {
                            data.push({
                                x: time + i * 1000,
                                y: parseInt(json.temp[i+19])
                                    });

                    }
                    //console.log(data);
                    return data;
                }())
            }]
        });
    });
};