from lxml import etree

# Load the XML file with full path
tree = etree.parse('/home/aaditya/btech/TY2/ade/books.xml')
root = tree.getroot()

# Get all book titles
print("************************ Books Titles **************************")
titles = root.xpath('//book/title/text()')
print("Titles:")
for title in titles:
    print(title)

# Get all book authors
print("\n\n")

print("************************ All Authors **************************")

authors = root.xpath('//book/author/text()')

for author in authors:
    print(author)

print("\n\n")
print("************************ Books by Genre with author **************************")
for book in root.xpath('//book'):
    author = book.xpath('author/text()')[0]
    genre = book.xpath('genre/text()')[0]
    print(f"Book: {author} - {genre}")


print("\n\n")
print("************************ Books by Genre with title **************************")
for genre in set(root.xpath('//book/genre/text()')):
    books_in_genre = root.xpath(f"//book[genre='{genre}']")
    print(f"Books in {genre}:")
    for book in books_in_genre:
        print(f"- {book.find('title').text}")
