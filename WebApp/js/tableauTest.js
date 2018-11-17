var driver = {
    complicate : false,
    path : "shared&#47;33SF8BXFP",
    static_image : "https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;33&#47;33SF8BXFP&#47;1.png",
    width: '1000px',
    height: '827px'
};

var passenger = {
    complicate : true,
    name : "ALTNATION&#47;AltNation",
    static_image : "https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;AL&#47;ALTNATION&#47;AltNation&#47;1.png",
    width: '1450px',
    height: '2527px'
};

var stuff = {
    complicate : true,
    name : "StarMap_1&#47;Dashboard3",
    static_image : "https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;St&#47;StarMap_1&#47;Dashboard3&#47;1.png",
    width: '1400px',
    height: '827px'
};

function changeDashboard(member){
    var divElement = document.getElementById('viz1542464115990');
    var str = '<object class="tableauViz" style="display:none;">' +
        '<param name="host_url" value="https%3A%2F%2Fpublic.tableau.com%2F" />' +
        '<param name="embed_code_version" value="3" />';
    if(member.complicate){
        str += '<param name="site_root" value="" />' +
        '<param name="name" value="' + member.name + '" />' +
        '<param name="tabs" value="no" />';
    }else{
        str += '<param name="path" value="' + member.path + '" />'
    }     
        str += '<param name="toolbar" value="yes" />' +
        '<param name="static_image" value="' + member.static_image + '" />' +
        '<param name="animate_transition" value="yes" />' +
        '<param name="display_static_image" value="yes" />' +
        '<param name="display_spinner" value="yes" />' +
        '<param name="display_overlay" value="yes" />' +
        '<param name="display_count" value="yes" />' +
        '</object>';
    divElement.innerHTML = str;
    var vizElement = divElement.getElementsByTagName('object')[0];
    vizElement.style.width = member.width;
    vizElement.style.height = member.height;
    var scriptElement = document.createElement('script');
    scriptElement.src = 'https://public.tableau.com/javascripts/api/viz_v1.js';
    vizElement.parentNode.insertBefore(scriptElement, vizElement);
}