        // <![CDATA[

        var jckdata_builds = document.getElementById('jckdata_builds').textContent.split(/\s*,\s*/).flatMap((s) => (s.trim()));
        var jckdata_errors = document.getElementById('jckdata_errors').textContent.split(/\s*,\s*/).flatMap((s) => (s.trim()));
        var jckdata_failed = document.getElementById('jckdata_failed').textContent.split(/\s*,\s*/).flatMap((s) => (s.trim()));
        var jckdata_total = document.getElementById('jckdata_total').textContent.split(/\s*,\s*/).flatMap((s) => (s.trim()));
        var jckdata_runned = document.getElementById('jckdata_runned').textContent.split(/\s*,\s*/).flatMap((s) => (s.trim()));
        var jckdata_imps = document.getElementById('jckdata_imps').textContent.split(/\s*,\s*/).flatMap((s) => (s.trim()));
        var jckdata_regs = document.getElementById('jckdata_regs').textContent.split(/\s*,\s*/).flatMap((s) => (s.trim()));

        var allJckFails = {
            labels: jckdata_builds,
                datasets: [
                {
                label: "Error",
                fillColor: "rgba(255,0,255,0.2)",
                strokeColor: "rgba(255,0,255,1)",
                pointColor: "rgba(255,0,255,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(255,0,255,1)",
                        data: jckdata_errors
                },
                {
                label: "Failed",
                fillColor: "rgba(255,0,0,0.2)",
                strokeColor: "rgba(255,0,0,1)",
                pointColor: "rgba(255,0,0,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(255,0,0,1)",
                        data: jckdata_failed
                }
                ]
        };
        var options = {
            bezierCurve: false,
            multiTooltipTemplate: "<%= datasetLabel + \": \" + value %>"
         }

        var ctx = document.getElementById("jckErrorsFailuresChart").getContext("2d");
        var jckErrorsChart = new Chart(ctx).Line(allJckFails, options);
        document.getElementById("jckErrorsFailuresChartContainer").onclick = function (evt) {
            var activePoints = jckErrorsChart.getPointsAtEvent(evt);
            var point = activePoints[0]
            var datasetIndex = point.datasetIndex //labels are for all data together,  no need to look into exact dataset
            var index = point.index
            var result = point.label
            var buildId = result.substring(result.lastIndexOf(":") + 1)
            window.open("" + buildId + "/java-reports", "_blank");
        };

        var allJck = {
            labels: jckdata_builds,
                datasets: [
                {
                label: "Tests total",
                fillColor: "rgba(180,180,180,0.2)",
                strokeColor: "rgba(180,180,180,1)",
                pointColor: "rgba(180,180,180,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(180,180,180,1)",
                        data: jckdata_total
                },
                {
                label: "Runned tests",
                fillColor: "rgba(180,180,180,0.2)",
                strokeColor: "rgba(180,180,180,1)",
                pointColor: "rgba(180,180,180,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(180,180,180,1)",
                        data: jckdata_runned
                }
                ]
        };
        var options = {
            bezierCurve: false,
            multiTooltipTemplate: "<%= datasetLabel + \": \" + value %>"
         }
        var ctx = document.getElementById("jckPassedChart").getContext("2d");
        var jckPassedChartTck = new Chart(ctx).Line(allJck, options);
        document.getElementById("jckPassedChartContainer").onclick = function (evt) {
            var activePoints = jckPassedChartTck.getPointsAtEvent(evt);
            var point = activePoints[0]
            var datasetIndex = point.datasetIndex //labels are for all data together,  no need to look into exact dataset
            var index = point.index
            var result = point.label
            var buildId = result.substring(result.lastIndexOf(":") + 1)
            window.open("" + buildId + "/java-reports", "_blank");
        };

        var allJckRegressions = {
            labels: jckdata_builds,
                datasets: [
                {
                label: "Improvements",
                fillColor: "rgba(0,220,0,0.5)",
                strokeColor: "rgba(0,220,0,0.8)",
                borderWidth: 2,
                barThickness: 20,
                hoverBackgroundColor: "rgba(0,220,0,0.75)",
                hoverBorderColor: "rgba(0,220,0,1)",
                        data: jckdata_imps
                },
                {
                label: "Regressions",
                fillColor: "rgba(220,0,0,0.5)",
                strokeColor: "rgba(220,0,0,0.8)",
                borderWidth: 2,
                barThickness: 20,
                hoverBackgroundColor: "rgba(220,0,0,0.75)",
                hoverBorderColor: "rgba(220,0,0,1)",
                        data: jckdata_regs
                }
                ]
        };
        var options = {
            bezierCurve: false,
            multiTooltipTemplate: "<%= datasetLabel + \": \" + value %>"
         }
        var ctx = document.getElementById("jckRegressionsChart").getContext("2d");
        var jckRegressions = new Chart(ctx).Bar(allJckRegressions, options);
        document.getElementById("jckRegressionsChartContainer").onclick = function (evt) {
            var activePoints = jckRegressions.getBarsAtEvent(evt);
            var point = activePoints[0]
            var datasetIndex = point.datasetIndex //labels are for all data together,  no need to look into exact dataset
            var index = point.index
            var result = point.label
            var buildId = result.substring(result.lastIndexOf(":") + 1)
            window.open("" + buildId + "/java-reports", "_blank");
        };

        //hopefully temporary workaround to weird sizes
        var jc1 = document.getElementById('jckErrorsFailuresChartContainer')
        jc1.style.width = "599px";
        jc1.style.height = "599px";
        var jc2 = document.getElementById('jckPassedChartContainer')
        jc2.style.width = "599px";
        jc2.style.height = "599px";
        var jc3 = document.getElementById('jckRegressionsChartContainer')
        jc3.style.width = "599px";
        jc3.style.height = "599px";
        // ]]>

