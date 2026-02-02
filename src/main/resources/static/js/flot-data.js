
//Flot Pie Chart
$(function() {

    var data = [{
        label: "POP",
        data: 16655
    }, {
        label: "Egg Money",
        data: 316
    }, {
        label: "Happy Money",
        data: 1042
    }, {
        label: "Others",
        data: 1759
    }, {
        label: "Googel Physical",
        data: 6629
    }, {
        label: "Google Digital",
        data: 379
    }, {
        label: "Local",
        data: 239
    }];

    var plotObj = $.plot($("#flot-pie-chart"), data, {
        series: {
            pie: {
                show: true
            }
        },
        grid: {
            hoverable: true
        },
        tooltip: true,
        tooltipOpts: {
            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
            shifts: {
                x: 20,
                y: 0
            },
            defaultTheme: false
        }
    });

});

//Flot Multiple Axes Line Chart
$(function() {
    var POP = [
        [1467331200000, 5657171362],
        [1468195200000, 3756075043],
        [1468800000000, 3338497636],
        [1469404800000, 3903864318]
    ];
    var EggMoney = [
        [1467331200000, 108657500],
        [1468195200000, 71098000],
        [1468800000000, 64550000],
        [1469404800000, 71893000]
    ];
    
    var HappyMoney = [
        [1467331200000, 1131402000],
        [1468195200000, 1069157000],
        [1468800000000, 2042278000],
        [1469404800000, 1799369000]
    ];
    
    var Others = [
        [1467331200000, 554780700],
        [1468195200000, 356561080],
        [1468800000000, 434735740],
        [1469404800000, 413255920]
    ];
    var GoogelPhysical = [
        [1467331200000, 2735577600],
        [1468195200000, 1936475000],
        [1468800000000, 991520000],
        [1469404800000, 966210000]
    ];
    var GoogleDigital = [
        [1467331200000, 121135000],
        [1468195200000, 89795000],
        [1468800000000, 83770000],
        [1469404800000, 85215000]
    ];
    var Local = [
        [1467331200000, 64621500],
        [1468195200000, 52746000],
        [1468800000000, 56770200],
        [1469404800000, 65345100]
    ];

    function euroFormatter(v, axis) {
        return v.toFixed(axis.tickDecimals);
    }

    function doPlot(position) {
        $.plot($("#flot-line-chart-multi"), [{
            data: POP,
            label: "POP"
        }, {
            data: EggMoney,
            label: "EggMoney"
        }, {
            data: HappyMoney,
            label: "HappyMoney"
        }, {
            data: Others,
            label: "Others"
        }, {
            data: GoogelPhysical,
            label: "GoogelPhysical"
        }, {
            data: GoogleDigital,
            label: "GoogleDigital"
        }, {
            data: Local,
            label: "Local"
        }], {
            xaxes: [{
                mode: 'time'
            }],
            yaxes: [{
                min: 0
            }, {
                // align if we are to the right
                alignTicksWithAxis: position == "right" ? 1 : null,
                position: position,
                tickFormatter: euroFormatter
            }],
            legend: {
                position: 'sw'
            },
            grid: {
                hoverable: true //IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s for %x was %y",
                xDateFormat: "%y-%m-%d",

                onHover: function(flotItem, $tooltipEl) {
                    // console.log(flotItem, $tooltipEl);
                }
            }

        });
    }

    doPlot("right");

    $("button").click(function() {
        doPlot($(this).text());
    });
});

//Flot Bar Chart

$(function() {

    var barOptions = {
        series: {
            bars: {
                show: true,
                barWidth: 500000000
            }
        },
        xaxis: {
            mode: "time",
            timeformat: "%m/%d",
            minTickSize: [4, "day"]
        },
        grid: {
            hoverable: true
        },
        legend: {
            show: false
        },
        tooltip: true,
        tooltipOpts: {
            content: "x: %x, y: %y"
        }
    };
    var barData = {
        label: "bar",
        data: [
            [1467331200000, 10373363162],
            [1468195200000, 7331907123],
            [1468800000000, 7012121576],
            [1469404800000, 7305152338]
        ]
    };
    $.plot($("#flot-bar-chart"), [barData], barOptions);

});
