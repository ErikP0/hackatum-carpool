var driver = {
    realName: '#driver',
    complicate: true,
    name: "Sixt_Dashboards&#47;Driver",
    static_image: "https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;Si&#47;Sixt_Dashboards&#47;Driver&#47;1.png",
    width: '650px',
    height: '887px'
};

var passenger = {
    realName: '#passenger',
    complicate: true,
    name: "Sixt_Dashboards&#47;User",
    static_image: "https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;Si&#47;Sixt_Dashboards&#47;User&#47;1.png",
    width: '650px',
    height: '887px'
};

var stuff = {
    realName: '#stuff',
    complicate: true,
    name: "Sixt_Dashboards&#47;Staff",
    static_image: "https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;Si&#47;Sixt_Dashboards&#47;Staff&#47;1.png",
    width: '1200px',
    height: '1027px'
};

function changeDashboard(member) {
    $('#channels li').removeClass('selected');
    $(member.realName).addClass('selected');
    var divElement = document.getElementById('dashboard');
    var str = '<object class="tableauViz" style="display:none;">' +
        '<param name="host_url" value="https%3A%2F%2Fpublic.tableau.com%2F" />' +
        '<param name="embed_code_version" value="3" />';
    if (member.complicate) {
        str += '<param name="site_root" value="" />' +
            '<param name="name" value="' + member.name + '" />' +
            '<param name="tabs" value="no" />';
    } else {
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
