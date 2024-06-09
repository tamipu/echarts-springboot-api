function renderChart(elementId, options) {
    let chartDom = document.getElementById(elementId);
    let myChart = echarts.init(chartDom);
    myChart.setOption(options);
}

function getTemplate(templateName) {
    return $.ajax({
        url: `http://localhost:8080/chart/templates?templateName=${templateName}`,
        type: "GET"
    });
}

function getSeries(chartName) {
    return $.ajax({
        url: `http://localhost:8080/chart/series?chartName=${chartName}`,
        type: "GET"
    }).done(data => {
        console.log("Series Data:", data);
    });
}


function fetchAndRenderChart(elementId, templateName, chartName, processSeries) {
    Promise.all([
        getTemplate(templateName),
        getSeries(chartName)
    ]).then(([templateData, seriesData]) => {
        console.log("Template Data:", templateData);
        console.log("Series Data:", seriesData);

        let options = JSON.parse(templateData);
        const processedData = processSeries(seriesData);

        console.log("Processed Data:", processedData);

        options.series[0].data = processedData;
        renderChart(elementId, options);
    }).catch(error => {
        console.error("Error fetching chart data:", error);
    });
}


function roundChartProcessSeries(seriesData) {
    const names = ["Search Engine", "Direct", "Email", "Union Ads"];
    return seriesData[0].data.map((value, index) => ({
        value: value,
        name: names[index]
    }));
}
function pieChartProcessSeries(seriesData) {
    console.log("Raw Series Data for Pie Chart:", seriesData);
    const names = ["Category A", "Category B", "Category C", "Category D"];
    return seriesData[0].data.map((value, index) => ({
        value: value,
        name: names[index]
    }));
}

function scatterProcessSeries(seriesData) {
    const xData = seriesData.find(serie => serie.name === "0").data;
    const yData = seriesData.find(serie => serie.name === "1").data;

    return xData.map((x, i) => [x, yData[i]]);
}


function fetchAndRenderChartColumn(elementId, templateName, chartName, processSeries) {
    Promise.all([
        getTemplate(templateName),
        getSeries(chartName)
    ]).then(([templateData, seriesData]) => {
        console.log("Template Data for Column Chart:", templateData);
        console.log("Series Data for Column Chart:", seriesData);

        let options = JSON.parse(templateData);
        const processedData = processSeries(seriesData);

        console.log("Processed Data for Column Chart:", processedData);

        options.series = processedData.series;
        options.legend.data = processedData.legend;
        options.xAxis.data = processedData.xAxis;

        renderChart(elementId, options);
    }).catch(error => {
        console.error("Error fetching chart data:", error);
    });
}


function stackedColumnProcessSeries(seriesData) {
    console.log("Raw Series Data for Stacked Column:", seriesData);

    const categories = ["Jan", "Feb", "Mar", "Apr", "May", "Jun"];
    const series = seriesData.map(serie => ({
        name: serie.name,
        type: 'bar',
        stack: 'total',
        data: serie.data
    }));
    const legend = seriesData.map(serie => serie.name);

    const processedData = {
        series: series,
        legend: legend,
        xAxis: categories
    };

    console.log("Processed Data for Stacked Column:", processedData);

    return processedData;
}

function lineAreaProcessSeries(seriesData) {
    return seriesData.map(serie => ({
        name: serie.name,
        type: 'line',
        stack: 'area',
        areaStyle: {},
        data: serie.data,
        yAxisIndex: serie.yAxisIndex || 0 // Ensure yAxisIndex is set to 0 if not provided
    }));
}

function boxplotProcessSeries(seriesData) {
    if (!Array.isArray(seriesData)) {
        console.error('Input seriesData is not an array.');
        return [];
    }

    const boxplotSeries = [];

    seriesData.forEach(series => {
        // Check if series data is available
        if (series.data && Array.isArray(series.data) && series.data.length > 0) {
            const boxplotData = series.data.map(dataPoint => {
                // Check if dataPoint has necessary structure
                if (Array.isArray(dataPoint) && dataPoint.length === 2 && Array.isArray(dataPoint[1]) && dataPoint[1].length === 5) {
                    return [dataPoint[0], dataPoint[1][0], dataPoint[1][1], dataPoint[1][2], dataPoint[1][3], dataPoint[1][4]];
                } else {
                    console.error('Invalid dataPoint format in series:', series.name);
                    return null;
                }
            }).filter(dataPoint => dataPoint !== null); // Remove invalid data points

            // Construct series object for boxplot chart
            const boxplotSeriesItem = {
                type: 'boxplot',
                name: series.name,
                data: boxplotData
            };

            // Add the series to the array
            boxplotSeries.push(boxplotSeriesItem);
        } else {
            console.error('Series data missing or invalid:', series.name);
        }
    });

    return boxplotSeries;
}


fetchAndRenderChart('chart1', 'semi-circle-gauche', 'semi-circle-gauche', roundChartProcessSeries);
fetchAndRenderChart('chart2', 'scatter', 'scatter', scatterProcessSeries);
fetchAndRenderChartColumn('chart4', 'stacked-column', 'stacked-column', stackedColumnProcessSeries);
fetchAndRenderChart('chart5', 'line-area', 'line-area', lineAreaProcessSeries);
fetchAndRenderChart('chart7', 'pie-chart', 'pie-chart', pieChartProcessSeries);
fetchAndRenderChart('chart8', 'boxplot', 'boxplot', boxplotProcessSeries);
