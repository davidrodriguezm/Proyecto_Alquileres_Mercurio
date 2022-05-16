google.charts.load('current', { 'packages': ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {

	fetch("/rest/tipos-vehiculo")
		.then(response => response.json())
		.then(response_data => piechart(response_data))
		.catch(function(error) {
			console.log('Error en petición GET: ' + error);
		});
		
		
	fetch("/rest/estado-vehiculos")
		.then(response => response.json())
		.then(response_data => donutchart(response_data))
		.catch(function(error) {
			console.log('Error en petición GET: ' + error);
		});
}

function piechart(jsonData) {
	var data = google.visualization.arrayToDataTable([
		['Task', 'Hours per Day'],
		['Monovolumen', jsonData.monovolumen],
		['Furgoneta', jsonData.furgoneta],
		['Mini camión', jsonData.miniCamion]
	]);

	var options = { 
		title: 'Tipos de Vehículos de la flota', 
		width: 500, 
		height: 300
	};
	
	var chart = new google.visualization.PieChart(document.getElementById('piechart'));
	chart.draw(data, options);
};

function donutchart(jsonData) {
	var data = google.visualization.arrayToDataTable([
		['Task', 'Hours per Day'],
		['Disponible', jsonData.disponible],
		['Averiado', jsonData.averiado],
		['Revisión', jsonData.revision],
		['Alquilado', jsonData.alquilado]
	]);

	var options = { 
		title: 'Estado de Vehículos en la flota', 
		pieHole: 0.4,
		width: 500, 
		height: 300
	};
		
	var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
	chart.draw(data, options);
};

document.getElementById("actualizar-datos").addEventListener("click", drawChart);