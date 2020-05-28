// Element the chart will be drawn to--> canvas element inside the html with id = Recommendation-Chart-Display
var canvas = document.getElementById('Prediction-Chart-Display');

// Data displayed to the graph
var Graphdata = {
    labels: [],
    datasets: [{
        borderColor: 'rgba(56, 132, 45,0.5)',
        backgroundColor:'rgba(56, 132, 45,0.2)',
        pointHitRadius: 2,
        data: []
    }]
};
// Loading bool used to check if the data has loaded
var loading = false;
// Time format for the X axis
var timeFormat = "MM/DD/YYYY HH:mm";
// variable used to store JSON requested data 
var jsonInfo;
// Display options for chart
var option = {
    elements: {
        point:{
            radius: 1
        }
    },
    maintainAspectRatio: false,
    responsive: true,
    legend: {
        display: false,
    },
    scales: {
        xAxes: [{
            type: "time",
            time: {
                parser: timeFormat,
                tooltipFormat: "ll HH:mm"
            },
            ticks: {
                maxRotation: 0
            }
        }],
    },
    animation: {
        duration: 1000,
        easing: "linear",
    },
    pan: {
        enabled: true,
        mode: "x",
        speed: 10,
        threshold: 10
    },
    zoom: {
        enabled: true,
        drag: false,
        mode: "x",
        limits: {
            max: 10,
            min: 0.5
        }
    }
};
// Chart variable
var predictionGraph = Chart.Line(canvas, {
    data: Graphdata,
    options: option
});

// Called on Document load, data will repeatedly check for new data 
document.addEventListener("DOMContentLoaded", function () {
    fetchData(loading);
    (function appendData() {
        setTimeout(function () {
            fetchData(loading)
            appendData()
        }, 15000);
    }());
});

// Adds Data to the chart data array
function addData(update) {
    if (!update) {
        if (Graphdata.labels.length == 0) {
            for (i in jsonInfo) {
                if (i != 0) {
                    Graphdata.labels.push(convertDate(jsonInfo[i].time)),
                        Graphdata.datasets[0].data.push(jsonInfo[i].value)
                }
            }
            loading = true;
            currencyGraph.update();
        }
    } else {
        if (convertDate(jsonInfo[jsonInfo.length - 1].time) != Graphdata.labels[Graphdata.labels.length - 1]) {
            Graphdata.labels.push(convertDate(jsonInfo[jsonInfo.length - 1].time)),
                Graphdata.datasets[0].data.push(jsonInfo[jsonInfo.length - 1].value)
            if(Graphdata.datasets[0].data.length > 5000){
                Graphdata.datasets[0].data.shift();
                Graphdata.labels.shift();
            }
            currencyGraph.update();
        }
    }
}

// Converts AM PM time to 24hr
function convertTime12to24(time12h){
    const [time, modifier] = time12h.split(' ');

    let [hours, minutes] = time.split(':');

    if (hours === '12') {
        hours = '00';
    }

    if (modifier === 'PM') {
        hours = parseInt(hours, 10) + 12;
    }

    return `${hours}:${minutes}`;
}

// Converts the data value given by the JSON data
function convertDate(value) {
    var dateTime = new Date(Math.floor(value * 1000))
    var time = this.convertTime12to24(dateTime.toLocaleTimeString());
    var date = dateTime.toLocaleDateString();
    return `${date} ${time}`;
}

// resets the users view of the graph
function resetZoom(){
    window.currencyGraph.resetZoom();
}

// Fetchs data from the REST API
function fetchData(update) {
    const url_string = window.location.href;
    const url = new URL(url_string);
    fetch('/api/currencies/' + url.searchParams.get("code"))
        .then(
            function (response) {
                if (response.status !== 200) {
                    console.log('Looks like there was a problem. Status Code: ' +
                        response.status);
                    return;
                }

                // Examine the text in the response
                response.json().then(function (data) {
                    jsonInfo = data.price;
                    addData(update);
                });
            }
        )
        .catch(function (err) {
            console.log('Fetch Error :-S', err);
        });
}