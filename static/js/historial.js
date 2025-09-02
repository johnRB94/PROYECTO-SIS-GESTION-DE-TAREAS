const KEY_HIST = 'sm_historial';
let HIST = [];
let modal;

document.addEventListener('DOMContentLoaded', function () {
  modal = new bootstrap.Modal(document.getElementById('detalleModal'));

  
  seedDemo();

  
  cargar().then(dibujar);

  // buscador + limpiar
  document.getElementById('q').addEventListener('input', dibujar);
  document.getElementById('btnLimpiar').addEventListener('click', function () {
    document.getElementById('q').value = '';
    dibujar();
  });
});

async function cargar() {
  try {
    const r = await fetch('/api/pedidos/historial');
    if (!r.ok) throw new Error();
    HIST = await r.json();
  } catch (e) {
    HIST = leerLocal();
  }
}

function dibujar() {
  const q = document.getElementById('q').value.toLowerCase().trim();
  const tbody = document.getElementById('histBody');
  const datos = (HIST || []).filter(function (r) {
    const txt = (r.modulo + ' ' + r.accion + ' ' + (r.detalle || '') + ' ' + r.usuario).toLowerCase();
    return !q || txt.indexOf(q) !== -1;
  });

  if (!datos.length) {
    tbody.innerHTML = '<tr><td colspan="6" class="text-center">Sin registros.</td></tr>';
    return;
  }

  var html = '';
  for (var i = 0; i < datos.length; i++) {
    var r = datos[i];
    html +=
      '<tr>' +
        '<td class="text-nowrap">' + fmt(r.fecha) + '</td>' +
        '<td>' + esc(r.modulo) + '</td>' +
        '<td>' + esc(r.accion) + '</td>' +
        '<td><div class="text-truncate" style="max-width:420px">' + esc(r.detalle || '') + '</div></td>' +
        '<td class="text-nowrap">' + esc(r.usuario) + '</td>' +
        '<td>' +
          '<button class="btn btn-sm btn-primary" onclick="ver(\'' + (r.id || '') + '\')">' +
            '<i class="fas fa-eye"></i> Ver' +
          '</button>' +
        '</td>' +
      '</tr>';
  }
  tbody.innerHTML = html;
}

function ver(id) {
  var r = (HIST || []).find(function (x) { return String(x.id) === String(id); });
  if (!r) return;
  document.getElementById('mFecha').textContent   = fmt(r.fecha);
  document.getElementById('mModulo').textContent  = r.modulo;
  document.getElementById('mAccion').textContent  = r.accion;
  document.getElementById('mUsuario').textContent = r.usuario;
  document.getElementById('mDetalle').textContent = r.detalle || '';
  modal.show();
}
window.ver = ver;

// ===== LocalStorage  =====
function leerLocal() {
  try { return JSON.parse(localStorage.getItem(KEY_HIST) || '[]'); }
  catch (e) { return []; }
}
function guardarLocal(arr) {
  localStorage.setItem(KEY_HIST, JSON.stringify(arr));
}
function seedDemo() {
  var arr = leerLocal();
  if (arr.length) return;
  var now = new Date();
  var a = { id: 'h1', fecha: now.toISOString(), modulo: 'Tareas', accion: 'Creó tarea', detalle: '“Ceviche mixto” prioridad Alta', usuario: 'Kevin' };
  var b = { id: 'h2', fecha: new Date(now.getTime() - 3600 * 1000).toISOString(), modulo: 'Inventario', accion: 'Ajuste stock', detalle: '+2 kg pescado, +1 kg limón', usuario: 'Ruth' };
  guardarLocal([a, b]);
}


window.pushHistory = function (e) {
  var fila = {
    id: String(Date.now()),
    fecha: e && e.fecha ? e.fecha : new Date().toISOString(),
    modulo: e && e.modulo ? e.modulo : 'Tareas',
    accion: e && e.accion ? e.accion : '',
    detalle: e && e.detalle ? e.detalle : '',
    usuario: e && e.usuario ? e.usuario : 'sistema'
  };
  var arr = leerLocal();
  arr.unshift(fila);
  guardarLocal(arr);
  HIST = arr;
  dibujar();
};

// ===== helpers cortos =====
function fmt(iso) {
  var d = new Date(iso);
  var p = function (n) { return String(n).padStart(2, '0'); };
  return p(d.getDate()) + '/' + p(d.getMonth() + 1) + '/' + d.getFullYear() + ' ' + p(d.getHours()) + ':' + p(d.getMinutes());
}
function esc(s) {
  return String(s).replace(/[&<>"]/g, function (c) { return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]; });
}