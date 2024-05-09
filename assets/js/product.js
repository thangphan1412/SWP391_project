

// main product

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


// catagory

var headerCatagoryItem = document.querySelectorAll('.header__sort-item');

for(var i = 0; i < 4; i++){
    headerCatagoryItem[i].onclick = function(){
        var headerCatagoryActive = document.querySelector('.header__sort-item--active');
        headerCatagoryActive.classList.remove('header__sort-item--active');
        this.classList.add('header__sort-item--active');
        shuffer();
    }

}

var homeFilterPage = document.querySelectorAll('.home-filter-page-btn');

homeFilterPage[0].onclick = function(){
    var currentPage = document.querySelector('.home-filter-page-now');
    if(currentPage.textContent != 1){
        currentPage.textContent = Number(currentPage.textContent) - 1;
        shuffer();
    }
    if(currentPage.textContent != 14){
        homeFilterPage[1].classList.remove('home-filter-page-btn--disable');
    }
    if(currentPage.textContent == 1){
        homeFilterPage[0].classList.add('home-filter-page-btn--disable');
    }
}
homeFilterPage[1].onclick = function(){
    var currentPage = document.querySelector('.home-filter-page-now');
    if(currentPage.textContent != 14){
        currentPage.textContent = Number(currentPage.textContent) + 1;
        shuffer();
    }
    if(currentPage.textContent != 1){
        homeFilterPage[0].classList.remove('home-filter-page-btn--disable');
    }
    if(currentPage.textContent == 14){
        homeFilterPage[1].classList.add('home-filter-page-btn--disable');
    }
}

