const period = document.getElementById('airdatepicker')
const form = document.getElementById('form')
const errorElement = document.getElementById('error')

form.addEventListener('submit', (e) => {
    let dates = period.value.split(':');
    let dateFrom = new Date(dates[0]);
    let dateTo = new Date(dates[1]);
    if (dateFrom<new Date()||dateFrom>=dateTo) {
        e.preventDefault()
        errorElement.innerText = 'uncorrected date'
    }
})