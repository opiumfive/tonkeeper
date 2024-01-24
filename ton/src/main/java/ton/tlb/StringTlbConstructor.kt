package ton.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import ton.extensions.loadRemainingBits

object StringTlbConstructor : TlbConstructor<String>(schema = "", id = null) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: String
    ) = cellBuilder {
        storeUInt(0, 32)
        storeBytes(value.toByteArray())
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): String = cellSlice {
        if (bits.size >= 32) {
            try {
                loadUInt32()
                return String(loadRemainingBits().toByteArray())
            } catch (ignored: Throwable) {}
        }
        return ""
    }
}