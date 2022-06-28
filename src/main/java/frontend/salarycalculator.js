
function createSalaryCalculatorRequest() {
    const salary = document.getElementById("salary").value;
    getRequest("http://localhost:8080/salarycalculator?salary=" + salary, "json", (err, data) => {
        if (err != null) {
            console.error(err);
        } else {
            document.getElementById("employerExpense").textContent = `${data.employerExpense} ` + "€";
            document.getElementById("socialTax").textContent = `${data.socialTax} ` + "€";
            document.getElementById("insuranceByEmployer").textContent = `${data.unemploymentInsuranceByEmployer} ` + "€";
            document.getElementById("grossSalary").textContent = `${data.grossSalary} ` + "€";
            document.getElementById("pension").textContent = `${data.pension} ` + "€";
            document.getElementById("insuranceByEmployee").textContent = `${data.unemploymentInsuranceByEmployee} ` + "€";
            document.getElementById("incomeTaxFree").textContent = `${data.taxExemption} ` + "€";
            document.getElementById("incomeTax").textContent = `${data.incomeTax} ` + "€";
            document.getElementById("netSalary").textContent = `${data.netSalary} ` + "€";
        }
    });
}



