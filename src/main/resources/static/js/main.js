function ActualizarHora() {
    var fecha = new Date();
    var segundos = fecha.getSeconds();
    var minutos = fecha.getMinutes();
    var horas = fecha.getHours();

    var elementoHoras = document.getElementById("pHoras");
    var elementoMinutos = document.getElementById("pMinutos");
    var elementoSegundos = document.getElementById("pSegundos");

    elementoHoras.textContent = horas.toString().padStart(2, "0");
    elementoMinutos.textContent = minutos.toString().padStart(2, "0");
    elementoSegundos.textContent = segundos.toString().padStart(2, "0");


}

// **NUEVA FUNCIÓN PARA MOSTRAR LA FECHA**
function mostrarFecha() {
    const elementoFecha = document.getElementById('pFechaCompleta');
    if (elementoFecha) {
        const ahora = new Date();

        // Opciones para formatear la fecha con el nombre del día en español
        const opciones = {
            weekday: 'long',
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        };

        // Obtener la fecha formateada
        let fechaFormateada = ahora.toLocaleDateString('es-ES', opciones);

        // Limpiar la cadena para obtener el formato exacto: "Viernes 21/11/2025"
        fechaFormateada = fechaFormateada.charAt(0).toUpperCase() + fechaFormateada.slice(1);
        fechaFormateada = fechaFormateada.replace(', ', ' '); // Eliminar la coma si existe

        elementoFecha.textContent = fechaFormateada;
    }
}

setInterval(ActualizarHora, 1000);

// **Ejecutar la función de fecha UNA SOLA VEZ al inicio.**
mostrarFecha();

// Lista de pistas de ejemplo (cliente). Puedes editar o ampliar.
// const sampleTracks = [
//     { title: 'Sonido Colegio', url: '/img/sonido5.mp3' },
//     { title: 'Ambiente 1', url: '/img/sonido5.mp3' }
// ];


document.addEventListener('DOMContentLoaded', function () {
    const links = document.querySelectorAll('[data-click-sound]');

    links.forEach(link => {
        link.addEventListener('click', (event) => {
            const clickSoundSrc = link.getAttribute('data-click-sound');
            if (clickSoundSrc) {
                event.preventDefault(); // Prevenir la navegación instantánea
                const clickSound = new Audio(clickSoundSrc);
                clickSound.play().then(() => {
                    setTimeout(() => {
                        window.location.href = link.href;
                    }, 200); // Ajustar el retraso según la duración del sonido
                }).catch(error => {
                    console.error('Error al reproducir el sonido:', error);
                    window.location.href = link.href; // Navegar de todos modos en caso de error
                });
            }
        });
    });

    // Manejar el envío del formulario de búsqueda (si existe en otra parte)
    const searchForm = document.getElementById('search-form');
    if (searchForm) {
        searchForm.addEventListener('submit', (event) => {
            const button = searchForm.querySelector('button[type="submit"]');
            const clickSoundSrc = button ? button.getAttribute('data-click-sound') : null;
            if (clickSoundSrc) {
                event.preventDefault(); // Prevenir el envío instantáneo
                const clickSound = new Audio(clickSoundSrc);
                clickSound.play().then(() => {
                    setTimeout(() => {
                        searchForm.submit();
                    }, 200);
                }).catch(error => {
                    console.error('Error al reproducir el sonido:', error);
                    searchForm.submit();
                });
            }
        });
    }

    const sampleTracks = [
        { title: "Tema 1: Always On My Mind", url: "/music/Always_On_My_Mind.mp3" },
        { title: "Tema 2: Bee Gees - Tragedy", url: "/music/Bee_Gees_-_Tragedy.mp3" },
        { title: "Tema 3: How Deep Is Your Love", url: "/music/How_Deep_Is_Your_Love.mp3" },
        { title: "Tema 4: More than a woman", url: "/music/More_than_a_woman.mp3" },
        { title: "Tema 5: My Way", url: "/music/My_Way.mp3" },
        // AÑADIR AQUÍ TODAS LAS DEMÁS PISTAS MP3 QUE TENGAS EN /static/music/
    ];

    // --- Lógica para búsqueda y reproducción de música en el nav ---
    const musicInput = document.getElementById('music-input');
    const musicSearchBtn = document.getElementById('music-search-btn');
    const musicResults = document.getElementById('music-results');
    const musicPlayBtn = document.getElementById('music-play-btn');
    const audioPlayer = document.getElementById('audio-player');
    const musicFileInput = document.getElementById('music-file-input');
    const musicUploadBtn = document.getElementById('music-upload-btn');
    const showPlaylistBtn = document.getElementById('show-playlist-btn');
    const playerProgress = document.getElementById('player-progress');
    const playerTime = document.getElementById('player-time');
    let serverMaxUploadBytes = 10485760; // default 10MB until we fetch config
    const uploadLimitEl = document.getElementById('upload-limit');








    if (uploadLimitEl) uploadLimitEl.textContent = 'Límite: ' + Math.round(serverMaxUploadBytes / (1024 * 1024)) + ' MB';

    // Obtener configuración del servidor (límite de subida)
    fetch('/api/music/config')
        .then(r => r.ok ? r.json() : null)
        .then(cfg => {
            if (cfg && cfg.maxUploadBytes) {
                serverMaxUploadBytes = Number(cfg.maxUploadBytes);
                if (uploadLimitEl) uploadLimitEl.textContent = 'Límite: ' + Math.round(serverMaxUploadBytes / (1024 * 1024)) + ' MB';
            }
        }).catch(() => { /* ignore: usar el default si falla */ });

    function renderResults(list) {
        if (!musicResults) return;
        musicResults.innerHTML = '';
        if (!list || list.length === 0) {
            const li = document.createElement('li');
            li.className = 'list-group-item';
            li.textContent = 'No se encontraron pistas.';
            musicResults.appendChild(li);
            return;
        }
        list.forEach((t) => {
            const li = document.createElement('li');
            li.className = 'list-group-item list-group-item-action';
            li.textContent = t.title;
            li.dataset.url = t.url;
            li.style.cursor = 'pointer';
            li.addEventListener('click', () => {
                setAudioSource(t.url);
            });
            musicResults.appendChild(li);
        });
    }

    function setAudioSource(url) {
        if (!audioPlayer) return;
        audioPlayer.src = url;
        audioPlayer.play().catch(err => console.error('Error al reproducir:', err));
        updatePlayButton();
        // Guardar en playlist
        try {
            const key = 'music_playlist_v1';
            const stored = localStorage.getItem(key);
            const list = stored ? JSON.parse(stored) : [];
            if (!list.find(i => i.url === url)) {
                list.push({ title: audioPlayer.dataset.title || url.split('/').pop(), url });
                localStorage.setItem(key, JSON.stringify(list));
            }
        } catch (e) { /* ignore */ }
    }

    function updatePlayButton() {
        if (!audioPlayer || !musicPlayBtn) return;
        if (audioPlayer.paused || audioPlayer.src === '') {
            musicPlayBtn.textContent = 'Play';
            musicPlayBtn.classList.remove('btn-danger');
            musicPlayBtn.classList.add('btn-success');
        } else {
            musicPlayBtn.textContent = 'Pause';
            musicPlayBtn.classList.remove('btn-success');
            musicPlayBtn.classList.add('btn-danger');
        }

        // musicPlayBtn





    }


    // Buscar en sampleTracks por título o URL
    function searchTracks(query) {
        if (!query) return sampleTracks;
        const q = query.toLowerCase();
        return sampleTracks.filter(t => t.title.toLowerCase().includes(q) || t.url.toLowerCase().includes(q));
    }






    if (musicSearchBtn) {
        musicSearchBtn.addEventListener('click', () => {
            const q = musicInput ? musicInput.value.trim() : '';

            // Si hay un query (q), forzamos la búsqueda y el renderizado (comportamiento original)
            if (q) {
                const results = searchTracks(q);
                renderResults(results); // Esta función mostrará la lista
            } else {
                // Si no hay query (solo se presiona el botón "Ver lista" sin buscar nada)
                // Usamos toggle para mostrar u ocultar la lista existente.
                // Si la lista está vacía, primero la llenamos con todos los temas.
                if (musicResults.innerHTML === '' || musicResults.classList.contains('d-none')) {
                    // Si está vacía u oculta, la llenamos y mostramos
                    renderResults(sampleTracks);
                } else {
                    // Si está visible, la ocultamos
                    musicResults.classList.add('d-none');
                }
            }

            // Opcional: Cambiar el texto del botón
            if (!musicResults.classList.contains('d-none')) {
                musicSearchBtn.textContent = 'Ocultar lista';
            } else {
                musicSearchBtn.textContent = 'Ver lista';
            }
        });
    }



    function renderResults(results) {
        const musicResults = document.getElementById('music-results');

        const volumeControl = document.getElementById('volume-control');
        volumeControl.addEventListener('input', () => {
            audioPlayer.volume = volumeControl.value;
        });



        if (!musicResults) return;

        musicResults.innerHTML = ''; // Limpiar lista anterior

        results.forEach(item => {
            const li = document.createElement('li');
            li.className = 'list-group-item';
            li.textContent = item.title;
            li.dataset.url = item.url;

            li.addEventListener('click', () => {
                setAudioSource(item.url); // Reproducir

                // Mostrar título actual
                // document.getElementById('current-track-title').textContent = item.title;

                // Quitar clase activa de todas las pistas
                document.querySelectorAll('#music-results .list-group-item')
                    .forEach(el => el.classList.remove('active-track'));

                // Agregar clase activa a la pista seleccionada
                li.classList.add('active-track');

                // Ocultar lista
                musicResults.classList.add('d-none');
            });

            musicResults.appendChild(li);
        });

        musicResults.classList.remove('d-none'); // Mostrar lista
    }






    if (musicPlayBtn) {
        musicPlayBtn.addEventListener('click', () => {
            if (!audioPlayer) return;
            if (audioPlayer.paused) {
                audioPlayer.play().catch(err => console.error('Error al reproducir:', err));
            } else {
                audioPlayer.pause();
            }
            updatePlayButton();
        });
    }


    function resetPlayerUI() {
        // Obtenemos los elementos de la interfaz, asegurando que existan
        const playerProgress = document.getElementById('player-progress');
        const playerTime = document.getElementById('player-time');

        if (playerProgress) playerProgress.style.width = '0%';
        if (playerTime) playerTime.textContent = '00:00 / 00:00';
    }



    if (audioPlayer) {
        audioPlayer.addEventListener('play', updatePlayButton);
        audioPlayer.addEventListener('pause', updatePlayButton);
        // audioPlayer.addEventListener('ended', updatePlayButton);
        // MODIFICACIÓN APLICADA: Llama a resetPlayerUI al terminar la canción
        audioPlayer.addEventListener('ended', () => {
            updatePlayButton(); // Mantiene el botón Play/Pause actualizado
            resetPlayerUI();    // Reinicia la barra de progreso
        });
        // Actualizar barra de progreso y tiempo
        audioPlayer.addEventListener('timeupdate', () => {
            if (!playerProgress || !playerTime) return;
            const cur = audioPlayer.currentTime || 0;
            const dur = audioPlayer.duration || 0;
            const percent = dur ? (cur / dur) * 100 : 0;
            playerProgress.style.width = percent + '%';
            playerTime.textContent = formatTime(cur) + ' / ' + (dur ? formatTime(dur) : '00:00');


        });
        audioPlayer.addEventListener('loadedmetadata', () => {
            if (!playerTime) return;
            const dur = audioPlayer.duration || 0;
            playerTime.textContent = '00:00 / ' + (dur ? formatTime(dur) : '00:00');



        });

    }

    function formatTime(sec) {
        const s = Math.floor(sec % 60).toString().padStart(2, '0');
        const m = Math.floor(sec / 60).toString().padStart(2, '0');
        return m + ':' + s;
    }

    // Upload handling with progress (XHR)
    if (musicUploadBtn && musicFileInput) {
        musicUploadBtn.addEventListener('click', () => {
            const f = musicFileInput.files && musicFileInput.files[0];
            if (!f) {
                alert('Seleccione un archivo de audio para subir.');
                return;
            }

            // Validación cliente-side del tamaño antes de iniciar la subida
            if (f.size > serverMaxUploadBytes) {
                const mb = Math.round(serverMaxUploadBytes / (1024 * 1024));
                alert('Archivo demasiado grande. Tamaño máximo permitido: ' + mb + ' MB. El archivo seleccionado tiene ' + Math.round(f.size / (1024 * 1024)) + ' MB.');
                return;
            }

            const form = new FormData();
            form.append('file', f);

            const xhr = new XMLHttpRequest();
            xhr.open('POST', '/api/music/upload');
            xhr.responseType = 'json';
            xhr.upload.onprogress = (e) => {
                if (e.lengthComputable) {
                    const pct = Math.round((e.loaded / e.total) * 100);
                    // mostrar como texto en el botón durante la subida
                    musicUploadBtn.textContent = 'Subiendo ' + pct + '%';
                }
            };
            xhr.onload = () => {
                musicUploadBtn.textContent = 'Subir';
                if (xhr.status >= 200 && xhr.status < 300) {
                    const json = xhr.response;
                    if (json && json.url) {
                        // Añadir al listado y reproducir
                        renderResults([{ title: json.title, url: json.url }]);
                        setAudioSource(json.url);
                        alert('Archivo subido correctamente: ' + json.title);
                    } else if (json && json.error) {
                        alert('Error: ' + json.error);
                    }
                } else {
                    // Intentar leer JSON de error devuelto por el servidor
                    const json = xhr.response;
                    if (json && json.error) {
                        alert('Error: ' + json.error);
                    } else if (xhr.status === 413) {
                        alert('Error: Archivo demasiado grande (límite del servidor).');
                    } else {
                        alert('Error al subir el archivo. Código: ' + xhr.status);
                    }
                }
            };
            xhr.onerror = () => {
                musicUploadBtn.textContent = 'Subir';
                alert('Error en la carga (network).');
            };
            xhr.send(form);
        });
    }

    // Mostrar / ocultar playlist guardada
    if (showPlaylistBtn) {
        showPlaylistBtn.addEventListener('click', () => {
            const key = 'music_playlist_v1';
            const stored = localStorage.getItem(key);
            const list = stored ? JSON.parse(stored) : [];
            if (!list.length) {
                alert('Lista vacía');
                return;
            }
            // Render simple modal-like prompt: build a small list in a new window? We'll render inline below results.
            // Toggle a playlist container
            let container = document.getElementById('playlist-container');

            if (container) {
                container.remove();
                return;
            }
            container = document.createElement('div');
            container.id = 'playlist-container';
            container.className = 'mt-2';
            const ul = document.createElement('ul');
            ul.className = 'list-group';
            list.forEach(item => {
                const li = document.createElement('li');
                li.className = 'list-group-item list-group-item-action';
                li.textContent = item.title;
                li.style.cursor = 'pointer';
                // li.addEventListener('click', () => setAudioSource(item.url));

                li.addEventListener('click', () => {
                    if (!audioPlayer) return;

                    audioPlayer.src = item.url; // Establece la fuente

                    audioPlayer.play().then(() => {
                        // ÉXITO: La reproducción comenzó

                        // Quitar clase activa de todas las pistas
                        document.querySelectorAll('#music-results .list-group-item')
                            .forEach(el => el.classList.remove('active-track'));

                        // Agregar clase activa a la pista seleccionada
                        li.classList.add('active-track');

                        // Ocultar lista (SOLO si reproduce correctamente)
                        musicResults.classList.add('d-none');

                    }).catch(error => {
                        // ERROR: La reproducción falló (ej: archivo no encontrado 404)
                        console.error('Fallo al iniciar la reproducción. URL:', item.url, 'Error:', error);
                        alert('Error al reproducir el tema. Revise la consola del navegador para detalles.');
                        // La lista no se oculta, permanece visible
                    });

                    // Como setAudioSource ya no es llamado, actualizamos el botón manualmente,Actualiza el botón de Play/Pause
                    updatePlayButton();
                });

                ul.appendChild(li);
            });
            container.appendChild(ul);
            musicResults.parentNode.insertBefore(container, musicResults.nextSibling);
        });
    }

    // Render initial sample tracks
    // renderResults(sampleTracks);


});
