demo = {

    initChartsPages: function() {
        chartColor = "#FFFFFF";

        ctx = document.getElementById('chartAge').getContext("2d");

        myChart = new Chart(ctx, {
            type: 'bar',

            data: {
                labels: ["10대미만", "10대", "20대", "30대", "40대", "50대", "60대", "70대", "80대", "90대","100대"],
                datasets: [
                    {
                        data: [ageCount.baby, ageCount.ten, ageCount.twenty, ageCount.thirty, ageCount.forty, ageCount.fifty, ageCount.sixty, ageCount.eighty, ageCount.ninety, ageCount.hundred],

                        backgroundColor: [
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                            "rgba(0,255,153,0.5)",
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                        ],


                        borderWidth: 2,
                        barThickness: 36,
                    },
                ]
            },
            options: {

                legend: {
                    display: false
                },
            }

        });


        ctx = document.getElementById('chartGender').getContext("2d");

        let gender = [];

        $.each(genderList, function(i,item){
           gender.push(item.count);
           if(item.gender == 'M'){
               $('#male').text('('+item.count+'명)');
           }else{
               $('#female').text('('+item.count+'명)');
           }
        });

        myChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['남', '여'],
                datasets: [{
                    pointRadius: 0,
                    pointHoverRadius: 0,
                    backgroundColor: [
                        '#4acccd',
                        '#ef8157'
                    ],
                    borderWidth: 0,
                    data: gender,
                }]
            },

            options: {

                legend: {
                    display: false
                },

                pieceLabel: {
                    render: 'percentage',
                    fontColor: ['white'],
                    precision: 2
                },

                tooltips: {
                    enabled: true
                },

                scales: {
                    yAxes: [{

                        ticks: {
                            display: false
                        },
                        gridLines: {
                            drawBorder: false,
                            zeroLineColor: "transparent",
                            color: 'rgba(255,255,255,0.05)'
                        }

                    }],

                    xAxes: [{
                        barPercentage: 1.6,
                        gridLines: {
                            drawBorder: false,
                            color: 'rgba(255,255,255,0.1)',
                            zeroLineColor: "transparent"
                        },
                        ticks: {
                            display: false,
                        }
                    }]
                },
            }
        });

        ctx = document.getElementById('chartDepartment').getContext("2d");

        let labels = [];

        let myDatas = [];

        $.each(departmentNameList, function(i, item){
            labels.push(item.name);

            let check = false;
            $.each(departmentList, function(j,department){
                if(item.id == department.departmentSeqNo){
                    myDatas.push(department.count);
                    check = true;
                }
            });

            if(check == false){
                myDatas.push(0);
            }

        });

        myChart = new Chart(ctx, {
            type: 'bar',

            data: {
                labels: labels,
                datasets: [
                    {
                        borderColor: "#6bd098",
                        backgroundColor: [
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                        ],
                        borderWidth: 2,
                        barThickness: 36,
                        data: myDatas,
                    },

                ]
            },
            options: {

                legend: {
                    display: false
                },
            }
        });

        ctx = document.getElementById('chartJob').getContext("2d");

        let jobLabels = [];

        let jobDatas = [];

        $.each(jobItem, function(i, item){
            jobLabels.push(item);

            let check = false;
            $.each(jobList, function(j,job){
                if(item == job.job){
                    jobDatas.push(job.count);
                    check = true;
                }
            });

            if(check == false){
                jobDatas.push(0);
            }

        });

        myChart = new Chart(ctx, {
            type: 'bar',

            data: {
                labels: jobLabels,
                datasets: [
                    {
                        borderColor: "#6bd098",
                        backgroundColor: [
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                        ],
                        borderWidth: 2,
                        barThickness: 36,
                        data: jobDatas,
                    },

                ]
            },
            options: {

                legend: {
                    display: false
                },
            }
        });

        ctx = document.getElementById('chartStar').getContext("2d");

        let starLabels = [];

        let starDatas = [];

        $.each(starItem, function(i, item){
            starLabels.push(item);

            let check = false;
            $.each(starList, function(j,star){
                if(item == star.star){
                    starDatas.push(star.count);
                    check = true;
                }
            });

            if(check == false){
                starDatas.push(0);
            }

        });

        myChart = new Chart(ctx, {
            type: 'bar',

            data: {
                labels: starLabels,
                datasets: [
                    {
                        borderColor: "#6bd098",
                        backgroundColor: [
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                        ],
                        borderWidth: 2,
                        barThickness: 36,
                        data: starDatas,
                    },

                ]
            },
            options: {

                legend: {
                    display: false
                },
            }
        });

        ctx = document.getElementById('chartSpecialUnit').getContext("2d");

        let unitLabels = [];

        let unitDatas = [];

        $.each(specialUnitItem, function(i, item){
            unitLabels.push(item);

            let check = false;
            $.each(specialUnitList, function(j,unit){
                if(item == unit.unit){
                    unitDatas.push(unit.count);
                    check = true;
                }
            });

            if(check == false){
                unitDatas.push(0);
            }

        });

        myChart = new Chart(ctx, {
            type: 'bar',

            data: {
                labels: unitLabels,
                datasets: [
                    {
                        borderColor: "#6bd098",
                        backgroundColor: [
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                            "red",
                            "rgba(255, 255, 0, 0.5)",
                            "purple",
                            "orange",
                            "lightblue",
                        ],
                        borderWidth: 2,
                        barThickness: 36,
                        data: unitDatas,
                    },

                ]
            },
            options: {

                legend: {
                    display: false
                },
            }
        });


    },

};

$(function(){
    demo.initChartsPages();
});