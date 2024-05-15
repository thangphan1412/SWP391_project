
function responsive() {
    var listItem = document.querySelectorAll('.home-product-item');
    var bodyWidth = document.body.clientWidth;
    var listItemLength = listItem.length;
    
    if(bodyWidth < 740) {
        for(var i = listItemLength - 1; i >= Math.floor(listItemLength / 2) * 2; i--) {
            listItem[i].remove();
        }
    }
    else if(bodyWidth < 1024) {
        for(var i = listItemLength - 1; i >= Math.floor(listItemLength / 4) * 4; i--) {
            listItem[i].remove();
        }
    }
}

function checkPageArrow(){
    var paginationLink = document.querySelectorAll('.pagination-item-link');
    if(document.querySelector('.pagination-item--active a').textContent == 1){
        paginationLink[0].classList.add('pagination-item-link--disable');
        if(paginationLink[0].attributes.href){
            paginationLink[0].attributes.removeNamedItem('href');
        }
    }
    else {
        paginationLink[0].classList.remove('pagination-item-link--disable');
        if(!paginationLink[0].attributes.href){
            paginationLink[0].href = '#';
        }
    }
    if (document.querySelector('.pagination-item--active a').textContent == 8){
        paginationLink[6].classList.add('pagination-item-link--disable');
        if(paginationLink[6].attributes.href){
            paginationLink[6].attributes.removeNamedItem('href');
        }
    } 
    else {
        paginationLink[6].classList.remove('pagination-item-link--disable');
        if(!paginationLink[6].attributes.href){
            paginationLink[6].href = '#';
        }
    }
}

let slider = document.querySelector('.slider .list');
let items = document.querySelectorAll('.slider .list .item');
let next = document.getElementById('next');
let prev = document.getElementById('prev');
let dots = document.querySelectorAll('.slider .dots li');

let lengthItems = items.length - 1;
let active = 0;
next.onclick = function(){
    active = active + 1 <= lengthItems ? active + 1 : 0;
    reloadSlider();
}
prev.onclick = function(){
    active = active - 1 >= 0 ? active - 1 : lengthItems;
    reloadSlider();
}
let refreshInterval = setInterval(()=> {next.click()}, 3000);
function reloadSlider(){
    slider.style.left = -items[active].offsetLeft + 'px';
    // 
    let last_active_dot = document.querySelector('.slider .dots li.active');
    last_active_dot.classList.remove('active');
    dots[active].classList.add('active');

    clearInterval(refreshInterval);
    refreshInterval = setInterval(()=> {next.click()}, 3000);

    
}

dots.forEach((li, key) => {
    li.addEventListener('click', ()=>{
         active = key;
         reloadSlider();
    })
})
window.onresize = function(event) {
    reloadSlider();
};

const productContainers = [...document.querySelectorAll('.product-container')];
const nxtBtn = [...document.querySelectorAll('.nxt-btn')];
const preBtn = [...document.querySelectorAll('.pre-btn')];

productContainers.forEach((item, i) => {
    let containerDimensions = item.getBoundingClientRect();
    let containerWidth = containerDimensions.width;

    nxtBtn[i].addEventListener('click', () => {
        item.scrollLeft += containerWidth;
    })

    preBtn[i].addEventListener('click', () => {
        item.scrollLeft -= containerWidth;
    })
})




