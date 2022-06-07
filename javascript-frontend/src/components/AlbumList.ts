import { Album } from '../types/AllAlbums'
import { doRequest } from '../utils/Request'
import { Component } from './Component'
import { eventBus } from './EventBus'
import { ToastMessage } from './ToastComponent'

export class AlbumList extends Component {
  private albums: Album[]
  private albumsOrder: number[]
  private activateDragDrop: boolean

  private dragAndDropSourceAlbumPk!: number

  private tableBody!: HTMLTableSectionElement

  private saveButton!: HTMLButtonElement
  private hideSaveButton = true

  constructor(
    mountElement: HTMLElement,
    albums: Album[],
    activateDragDrop: boolean
  ) {
    super(mountElement)
    this.albums = albums
    this.albumsOrder = this.albums.map((album) => album.albumPk)
    this.activateDragDrop = activateDragDrop
  }

  protected mounted(): void {
    const table = document.createElement('table')
    table.classList.add('table')
    table.classList.add('table-hover')

    const thead = document.createElement('thead')
    this.tableBody = document.createElement('tbody')

    table.appendChild(thead)
    table.appendChild(this.tableBody)

    thead.innerHTML = `
    <tr>
        <th scope="col">Title</th>
        <th scope="col">Date</th>
        <th scope="col">Owner</th>
    </tr>`

    this.saveButton = document.createElement('button')
    this.saveButton.classList.add('btn')
    this.saveButton.classList.add('btn-primary')
    this.saveButton.textContent = 'Save Order'
    this.saveButton.onclick = async () => {
      try {
        await doRequest('/albumOrder', 'POST', { order: this.albumsOrder })
        this.hideSaveButton = true
        eventBus.notifySubscribers('toast', {
          isError: false,
          message: 'Album Order Saved',
        } as ToastMessage)
        this.update()
      } catch (error) {
        eventBus.notifySubscribers('toast', {
          isError: true,
          message: error,
        } as ToastMessage)
      }
    }

    this.mountElement.appendChild(table)
    if (this.activateDragDrop) this.mountElement.appendChild(this.saveButton)
  }

  protected showState(): void {
    this.tableBody.innerHTML = ''

    this.saveButton.hidden = this.hideSaveButton

    this.albums.forEach((album) => {
      const albumRow = document.createElement('tr')

      if (this.activateDragDrop) {
        function getAlbumPkFromDrag(event: DragEvent) {
          return parseInt(
            (event.target as HTMLElement)
              .closest('tr')!
              .getAttribute('albumpk')!
          )
        }

        albumRow.ondragstart = (event) => {
          this.dragAndDropSourceAlbumPk = getAlbumPkFromDrag(event)!
        }

        albumRow.ondragover = (event) => event.preventDefault()

        albumRow.ondrop = (event) => {
          const dropAlbumPk = getAlbumPkFromDrag(event)!

          if (dropAlbumPk === this.dragAndDropSourceAlbumPk) {
            event.preventDefault()
            return
          }

          const firstIndex = this.albumsOrder.indexOf(
            this.dragAndDropSourceAlbumPk
          )
          const secondIndex = this.albumsOrder.indexOf(dropAlbumPk)

          function swap(arr: any[], index1: number, index2: number) {
            const temp = arr[index1]
            arr[index1] = arr[index2]
            arr[index2] = temp
          }

          swap(this.albums, firstIndex, secondIndex)
          swap(this.albumsOrder, firstIndex, secondIndex)

          this.hideSaveButton = false
          this.update()
        }
        albumRow.draggable = true
        albumRow.setAttribute('albumpk', album.albumPk.toString())
      }

      const thTitle = document.createElement('th')
      const titleAnchor = document.createElement('a')
      titleAnchor.href = '#'
      titleAnchor.innerText = album.title
      titleAnchor.onclick = (event) => {
        event.preventDefault()
        eventBus.notifySubscribers('openAlbum', album.albumPk)
      }
      thTitle.appendChild(titleAnchor)

      const thDate = document.createElement('td')
      thDate.textContent = album.date

      const thOwner = document.createElement('td')
      thOwner.textContent = album.ownerUsername

      albumRow.appendChild(thTitle)
      albumRow.appendChild(thDate)
      albumRow.appendChild(thOwner)
      this.tableBody.appendChild(albumRow)
    })
  }
}
