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
    });
}

function fetchAndRenderChart(elementId, templateName, chartName, processSeries) {
    Promise.all([
        getTemplate(templateName),
        getSeries(chartName)
    ]).then(([templateData, seriesData]) => {
        let options = JSON.parse(templateData);
        options.series[0].data = processSeries(seriesData);
        renderChart(elementId, options);
    }).catch(error => {
        console.error("Error fetching chart data:", error);
    });
}

function roundChartProcessSeries(seriesData) {
    const names = ["Search Engine", "Direct", "Email", "Union Ads", "Video Ads"];
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
        let options = JSON.parse(templateData);
        const processedData = processSeries(seriesData);
        options.series = processedData.series;
        options.legend.data = processedData.legend;
        options.xAxis.data = processedData.xAxis;
        renderChart(elementId, options);
    }).catch(error => {
        console.error("Error fetching chart data:", error);
    });
}

function stackedColumnProcessSeries(seriesData) {
    const categories = ["Jan", "Feb", "Mar", "Apr", "May", "Jun"];
    const series = seriesData.map(serie => ({
        name: serie.name,
        type: 'bar',
        stack: 'total',
        data: serie.data
    }));
    const legend = seriesData.map(serie => serie.name);

    return {
        series: series,
        legend: legend,
        xAxis: categories
    };
}

fetchAndRenderChart('chart2', 'scatter', 'scatter', scatterProcessSeries);
fetchAndRenderChart('chart1', 'semi-circle-gauche', 'semi-circle-gauche', roundChartProcessSeries);
fetchAndRenderChartColumn('chart4', 'stacked-column', 'stacked-column', stackedColumnProcessSeries);
