export async function download(addr, filename) {
  const ipfsClient = require('ipfs-http-client')
  const ipfs = ipfsClient('/ip4/154.8.197.60/tcp/8001')

  const res = await ipfs.get(addr)
  const streamSaver = require('streamsaver')
  const fileStream = streamSaver.createWriteStream(filename)
  const writer = fileStream.getWriter()
  for await (const f of res) {
    for await (const c of f.content) {
      writer.write(c)
    }
  }
  writer.close()
}
