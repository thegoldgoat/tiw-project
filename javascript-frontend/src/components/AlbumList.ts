import { Album } from '../types/AllAlbums'
import { Component } from './Component'

export class AlbumList extends Component {
  private albums: Album[]

  constructor(mountElement: HTMLElement, albums: Album[]) {
    super(mountElement)
    this.albums = albums
  }

  protected mounted(): void {
    const table = document.createElement('table')
    table.classList.add('table')
    table.classList.add('table-hover')

    const thead = document.createElement('thead')
    const tbody = document.createElement('tbody')

    table.appendChild(thead)
    table.appendChild(tbody)

    thead.innerHTML = `
    <tr>
        <th scope="col">Title</th>
        <th scope="col">Date</th>
        <th scope="col">Owner</th>
    </tr>`

    this.albums.forEach((album) => {
      const albumRow = document.createElement('tr')

      const thTitle = document.createElement('th')
      const titleAnchor = document.createElement('a')
      titleAnchor.href = '#'
      titleAnchor.innerText = album.title
      titleAnchor.onclick = (event) => {
        event.preventDefault()
        console.log(`Clicked on ${album.title} (${album.albumPk})`)
      }
      thTitle.appendChild(titleAnchor)

      const thDate = document.createElement('td')
      thDate.textContent = album.date

      const thOwner = document.createElement('td')
      thOwner.textContent = album.ownerUsername

      albumRow.appendChild(thTitle)
      albumRow.appendChild(thDate)
      albumRow.appendChild(thOwner)
      tbody.appendChild(albumRow)
    })

    this.mountElement.appendChild(table)
  }

  protected showState(): void {}
}
